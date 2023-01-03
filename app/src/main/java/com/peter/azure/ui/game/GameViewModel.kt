package com.peter.azure.ui.game

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.azure.data.entity.*
import com.peter.azure.data.repository.NoteRepository
import com.peter.azure.data.repository.PreferencesRepository
import com.peter.azure.data.repository.PuzzleRepository
import com.peter.azure.data.repository.SudokuRepository
import com.peter.azure.util.azureSchedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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

    private val _gameUiState: MutableState<GameUiState> = mutableStateOf(GameUiState.Loading)
    val gameUiState: State<GameUiState> = _gameUiState

    private val noteListState = mutableStateOf(emptyList<Note>())
    private val numState = mutableStateOf(0)

    private var task: TimerTask? = null

    private fun scheduleLimit(job: Job) = azureSchedule {
        if (_gameUiState.value is GameUiState.Loading) {
            _gameUiState.value = GameUiState.Error(
                DataResult.Error.Code.UNKNOWN
            )
            job.cancel()
        }
    }

    fun selectLocation(location: Location, currentNum: Int) {
        val uiState = _gameUiState.value
        if (uiState is GameUiState.Playing) {
            if (uiState.location == location) {
                _gameUiState.value = uiState.copy(
                    location = Location(-1, -1)
                )
            } else {
                var markList = Mark.getDefaultList()
                val note = noteListState.value.find { it.location == location }
                if (note != null) {
                    markList = note.markList
                }
                numState.value = currentNum
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
                numState.value = num
                val updatedBoard = uiState.puzzle.board.toMutableList().map {
                    it.toMutableList()
                }
                updatedBoard[uiState.location.y][uiState.location.x] = Cell(num, Cell.Type.BLANK)
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

    fun makeNote(mark: Mark) {
        val uiState = _gameUiState.value
        if (uiState is GameUiState.Playing) {
            if (uiState.location.isNotDefault()) {
                viewModelScope.launch {
                    val currentNote = noteListState.value.find {
                        it.location == uiState.location
                    }
                    if (currentNote != null) {
                        val currentNoteIndex = noteListState.value.indexOf(currentNote)
                        val markList = currentNote.markList.toMutableList()
                        if (numState.value != 0) {
                            markList[numState.value - 1] = mark
                            val note = Note(currentNote.location, markList)
                            noteRepository.updateNote(note)
                            val noteList = noteListState.value.toMutableList()
                            noteList[currentNoteIndex] = note
                            noteListState.value = noteList
                            _gameUiState.value = uiState.copy(
                                markList = markList
                            )
                            writeNum(0)
                        }
                    }
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
            val job = viewModelScope.launch {
                val isCorrect = sudokuRepository.checkAnswer(uiState.puzzle)
                _gameUiState.value = uiState.copy(
                    isCorrect = isCorrect,
                    dialog = GameUiState.Playing.Dialog.Submit
                )
                task?.cancel()
            }
            task = scheduleLimit(job)
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

    fun requestDelete() {
        val uiState = _gameUiState.value
        if (uiState is GameUiState.Playing) {
            _gameUiState.value = uiState.copy(
                dialog = GameUiState.Playing.Dialog.Delete
            )
        }
    }

    fun deleteGame() {
        val uiState = _gameUiState.value
        if (uiState is GameUiState.Playing) {
            _gameUiState.value = GameUiState.Loading
            val job = viewModelScope.launch {
                noteRepository.deleteNotes()
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
        }
    }

    init {
        val gameLevel = savedStateHandle.get<String>("gameLevel") ?: ""
        val job = viewModelScope.launch {
            val puzzle: Puzzle
            val noteList: List<Note>
            if (gameLevel.isNotEmpty() && gameLevel != "empty") {
                val level = GameLevel.valueOf(gameLevel)
                val sudoku = sudokuRepository.getBoard(level)
                noteList = mutableListOf()
                val markList = Mark.getDefaultList()
                val board = sudoku.mapIndexed { y, row ->
                    row.mapIndexed { x, num ->
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
                preferencesRepository.setGameExistedState(true)
            } else {
                puzzle = puzzleRepository.getPuzzle()
                noteList = noteRepository.getNoteList()
            }

            noteListState.value = noteList
            _gameUiState.value = GameUiState.Playing(
                puzzle = puzzle,
                location = Location(-1, -1),
                markList = Mark.getDefaultList(),
                dialog = GameUiState.Playing.Dialog.None,
                isCorrect = false
            )
            task?.cancel()
        }
        task = scheduleLimit(job)
    }

}