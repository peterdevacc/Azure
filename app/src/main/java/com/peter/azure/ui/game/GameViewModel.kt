/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.azure.data.entity.*
import com.peter.azure.data.repository.NoteRepository
import com.peter.azure.data.repository.PreferencesRepository
import com.peter.azure.data.repository.PuzzleRepository
import com.peter.azure.data.repository.SudokuRepository
import com.peter.azure.data.util.DataResult
import com.peter.azure.data.util.GAME_LEVEL_SAVED_KEY
import com.peter.azure.util.azureSchedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val puzzleRepository: PuzzleRepository,
    private val noteRepository: NoteRepository,
    private val sudokuRepository: SudokuRepository,
    private val preferencesRepository: PreferencesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _gameUiState = MutableStateFlow<GameUiState>(GameUiState.Loading)
    val gameUiState = _gameUiState.asStateFlow()

    private val noteListState = mutableListOf<Note>()
    private var numState = 0

    private var task: TimerTask? = null

    fun selectLocation(location: Location, currentNum: Int) {
        val uiState = _gameUiState.value
        if (uiState is GameUiState.Playing) {
            if (uiState.location == location) {
                _gameUiState.value = uiState.copy(
                    location = Location.getDefault()
                )
            } else {
                var markList = Mark.getDefaultList()
                val note = noteListState.find { it.location == location }
                if (note != null) {
                    markList = note.markList
                }
                numState = currentNum
                _gameUiState.value = uiState.copy(
                    location = location,
                    markList = markList
                )
            }
        }
    }

    fun writeNum(num: Int) {
        val uiState = _gameUiState.value
        if (uiState is GameUiState.Playing) {
            if (uiState.location.isNotDefault()) {
                numState = num
                val updatedBoard = uiState.puzzle.board.map {
                    it.toMutableList()
                }
                updatedBoard[uiState.location.x][uiState.location.y] =
                    Cell(num, Cell.Type.BLANK)
                val updatePuzzle = Puzzle(board = updatedBoard)
                viewModelScope.launch {
                    puzzleRepository.updatePuzzle(updatePuzzle)
                }

                _gameUiState.value = uiState.copy(
                    puzzle = updatePuzzle
                )
            }
        }
    }

    fun blankLocation() {
        writeNum(0)
    }

    fun makeMark(mark: Mark) {
        val uiState = _gameUiState.value
        if (uiState is GameUiState.Playing) {
            val currentNum = numState
            if (uiState.location.isNotDefault() && currentNum != 0) {
                val currentNote = noteListState.find {
                    it.location == uiState.location
                }
                if (currentNote != null) {
                    val currentNoteIndex = noteListState.indexOf(currentNote)
                    val markList = currentNote.markList.toMutableList()
                    markList[currentNum - 1] = mark
                    val note = Note(currentNote.location, markList)
                    noteListState[currentNoteIndex] = note

                    val updatedBoard = uiState.puzzle.board.map {
                        it.toMutableList()
                    }
                    updatedBoard[currentNote.location.y][currentNote.location.x] =
                        Cell(0, Cell.Type.BLANK)
                    val updatePuzzle = Puzzle(board = updatedBoard)

                    viewModelScope.launch {
                        noteRepository.updateNote(note)
                        puzzleRepository.updatePuzzle(updatePuzzle)
                    }

                    _gameUiState.value = uiState.copy(
                        puzzle = updatePuzzle,
                        markList = markList
                    )

                    numState = 0
                }
            }
        }
    }

    fun submitAnswer() {
        val uiState = _gameUiState.value
        if (uiState is GameUiState.Playing) {
            _gameUiState.value = uiState.copy(
                dialog = GameUiState.Playing.Dialog.Processing
            )
            val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
                val isCorrect = sudokuRepository.checkBoardAnswer(uiState.puzzle.board)
                _gameUiState.value = uiState.copy(
                    isCorrect = isCorrect,
                    dialog = GameUiState.Playing.Dialog.Submit
                )
                task?.cancel()
            }
            task = scheduleLimit(job)
            job.start()
        }
    }

    fun deleteGame() {
        val uiState = _gameUiState.value
        if (uiState is GameUiState.Playing) {
            _gameUiState.value = GameUiState.Loading
            val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
                noteRepository.deleteAllNote()
                puzzleRepository.deletePuzzle()
                when (val result = preferencesRepository.setGameExistedState(false)) {
                    is DataResult.Error -> {
                        _gameUiState.value = GameUiState.Error(result.code)
                    }
                    is DataResult.Success -> {
                        _gameUiState.value = GameUiState.GameEnded
                    }
                }
                task?.cancel()
            }
            task = scheduleLimit(job)
            job.start()
        }
    }

    fun requestDelete() {
        val uiState = _gameUiState.value
        if (uiState is GameUiState.Playing) {
            _gameUiState.value = uiState.copy(
                dialog = GameUiState.Playing.Dialog.Delete
            )
        }
    }

    fun closeDialog() {
        val uiState = _gameUiState.value
        if (uiState is GameUiState.Playing) {
            _gameUiState.value = uiState.copy(
                dialog = GameUiState.Playing.Dialog.None
            )
        }
    }

    private fun scheduleLimit(job: Job) = azureSchedule {
        if (_gameUiState.value is GameUiState.Loading) {
            _gameUiState.value = GameUiState.Error(
                DataResult.Error.Code.UNKNOWN
            )
            job.cancel()
        }
    }

    init {
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            when (val gameExistedResult = preferencesRepository.getGameExistedStateLatest()) {
                is DataResult.Error -> {
                    _gameUiState.value = GameUiState.Error(
                        DataResult.Error.Code.UNKNOWN
                    )
                }
                is DataResult.Success -> {
                    val puzzle: Puzzle
                    val noteList: List<Note>
                    var prefFailed: Boolean? = null
                    if (!gameExistedResult.result) {
                        val gameLevel: String = checkNotNull(
                            savedStateHandle[GAME_LEVEL_SAVED_KEY]
                        )
                        val level = GameLevel.valueOf(gameLevel)
                        val sudoku = sudokuRepository.getPuzzleBoard(level)
                        noteList = mutableListOf()
                        val markList = Mark.getDefaultList()
                        val board = sudoku.mapIndexed { y, column ->
                            column.mapIndexed { x, num ->
                                val type = if (num == 0) {
                                    val note = Note(Location(y, x), markList)
                                    noteList.add(note)
                                    Cell.Type.BLANK
                                } else {
                                    Cell.Type.QUESTION
                                }
                                Cell(num, type)
                            }
                        }
                        puzzle = Puzzle(board = board)

                        noteRepository.insertNoteList(noteList)
                        puzzleRepository.insertPuzzle(puzzle)
                        prefFailed = when (preferencesRepository.setGameExistedState(true)) {
                            is DataResult.Error -> {
                                true
                            }
                            is DataResult.Success -> {
                                false
                            }
                        }
                    } else {
                        puzzle = puzzleRepository.getPuzzle()
                        noteList = noteRepository.getNoteList()
                    }

                    if (prefFailed == null || !prefFailed) {
                        noteListState.addAll(noteList)
                        _gameUiState.value = GameUiState.Playing(
                            puzzle = puzzle,
                            location = Location.getDefault(),
                            markList = Mark.getDefaultList(),
                            dialog = GameUiState.Playing.Dialog.None,
                            isCorrect = false
                        )
                    } else {
                        _gameUiState.value = GameUiState.Error(
                            DataResult.Error.Code.UNKNOWN
                        )
                    }
                }
            }
            task?.cancel()
        }
        task = scheduleLimit(job)
        job.start()
    }

}