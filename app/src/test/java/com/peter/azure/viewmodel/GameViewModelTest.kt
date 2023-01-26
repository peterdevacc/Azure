package com.peter.azure.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.peter.azure.data.entity.*
import com.peter.azure.data.repository.*
import com.peter.azure.data.util.DataResult
import com.peter.azure.data.util.GAME_LEVEL_SAVED_KEY
import com.peter.azure.ui.game.GameUiState
import com.peter.azure.ui.game.GameViewModel
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GameViewModelTest {

    private val puzzleRepository = mockk<PuzzleRepository>(relaxed = true)
    private val noteRepository = mockk<NoteRepository>(relaxed = true)
    private val preferencesRepository = mockk<PreferencesRepository>(relaxed = true)
    private val sudokuRepository = mockk<SudokuRepository>(relaxed = true)

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val magicNum = 600L

    private val board = listOf(
        listOf(0, 5)
    )
    private val puzzle = Puzzle(
        board = listOf(
            listOf(
                Cell(0, Cell.Type.BLANK), Cell(5, Cell.Type.QUESTION)
            )
        )
    )
    private val note = Note(Location(0, 0), Mark.getDefaultList())

    @Test
    fun `select location`() = runBlocking {
        launch(Dispatchers.Main) {
            coEvery {
                preferencesRepository.getGameExistedStateLatest()
            } returns DataResult.Success(false)
            coEvery {
                sudokuRepository.getPuzzleBoard(GameLevel.EASY)
            } returns board
            coEvery {
                preferencesRepository.setGameExistedState(true)
            } returns DataResult.Success("")
            val savedStateHandle = SavedStateHandle(
                mapOf(GAME_LEVEL_SAVED_KEY to GameLevel.EASY.name)
            )
            val viewModel = GameViewModel(
                puzzleRepository, noteRepository, sudokuRepository, preferencesRepository,
                savedStateHandle
            )
            delay(magicNum)

            viewModel.selectLocation(note.location, 0)
            var result = viewModel.gameUiState.value as GameUiState.Playing
            assertEquals(note.location, result.location)
            assertEquals(note.markList, result.markList)
            viewModel.selectLocation(note.location, 0)
            result = viewModel.gameUiState.value as GameUiState.Playing
            assertEquals(Location.getDefault(), result.location)
        }
        Unit
    }

    @Test
    fun `write num`() = runBlocking {
        launch(Dispatchers.Main) {
            coEvery {
                preferencesRepository.getGameExistedStateLatest()
            } returns DataResult.Success(false)
            coEvery {
                sudokuRepository.getPuzzleBoard(GameLevel.EASY)
            } returns board
            coEvery {
                preferencesRepository.setGameExistedState(true)
            } returns DataResult.Success("")
            val savedStateHandle = SavedStateHandle(
                mapOf(GAME_LEVEL_SAVED_KEY to GameLevel.EASY.name)
            )
            val viewModel = GameViewModel(
                puzzleRepository, noteRepository, sudokuRepository, preferencesRepository,
                savedStateHandle
            )
            delay(magicNum)
            clearAllMocks()

            val expected = Puzzle(
                board = listOf(
                    listOf(
                        Cell(1, Cell.Type.BLANK), Cell(5, Cell.Type.QUESTION)
                    )
                )
            )
            // has not selected location
            viewModel.writeNum(1)
            delay(magicNum)
            coVerify(exactly = 0) {
                puzzleRepository.updatePuzzle(expected)
            }
            confirmVerified(puzzleRepository)
            clearAllMocks()

            // has selected location
            viewModel.selectLocation(note.location, 0)
            viewModel.writeNum(1)
            delay(magicNum)
            coVerify(exactly = 1) {
                puzzleRepository.updatePuzzle(expected)
            }
            confirmVerified(puzzleRepository)
            val result = (viewModel.gameUiState.value as GameUiState.Playing).puzzle
            assertEquals(expected, result)
        }
        Unit
    }

    @Test
    fun `make mark`() = runBlocking {
        launch(Dispatchers.Main) {
            val num = 2

            coEvery {
                preferencesRepository.getGameExistedStateLatest()
            } returns DataResult.Success(false)
            coEvery {
                sudokuRepository.getPuzzleBoard(GameLevel.EASY)
            } returns board
            coEvery {
                preferencesRepository.setGameExistedState(true)
            } returns DataResult.Success("")
            val savedStateHandle = SavedStateHandle(
                mapOf(GAME_LEVEL_SAVED_KEY to GameLevel.EASY.name)
            )
            val viewModel = GameViewModel(
                puzzleRepository, noteRepository, sudokuRepository, preferencesRepository,
                savedStateHandle
            )
            delay(magicNum)
            clearAllMocks()

            // has not selected location
            viewModel.makeMark(Mark.Potential)
            coVerify(exactly = 0) {
                noteRepository.updateNote(note)
                puzzleRepository.updatePuzzle(puzzle)
            }
            confirmVerified(noteRepository, puzzleRepository)
            clearAllMocks()

            // has selected location
            val updatedMarkList = note.markList.toMutableList()
            updatedMarkList[num - 1] = Mark.Potential
            val updatedNote = Note(note.location, updatedMarkList)
            viewModel.selectLocation(updatedNote.location, num)
            viewModel.writeNum(num)
            delay(magicNum)
            clearAllMocks()

            // has selected location
            viewModel.makeMark(Mark.Potential)
            delay(magicNum)
            coVerify(exactly = 1) {
                noteRepository.updateNote(updatedNote)
                puzzleRepository.updatePuzzle(puzzle)
            }
            confirmVerified(noteRepository, puzzleRepository)
            clearAllMocks()

            // num has been reset to 0 after a successful invocation of makeMark
            viewModel.makeMark(Mark.WRONG)
            delay(magicNum)
            coVerify(exactly = 0) {
                noteRepository.updateNote(updatedNote)
                puzzleRepository.updatePuzzle(puzzle)
            }
            confirmVerified(noteRepository, puzzleRepository)
        }
        Unit
    }

    @Test
    fun `submit answer`() = runBlocking {
        launch(Dispatchers.Main) {
            coEvery {
                preferencesRepository.getGameExistedStateLatest()
            } returns DataResult.Success(false)
            coEvery {
                sudokuRepository.getPuzzleBoard(GameLevel.EASY)
            } returns board
            coEvery {
                preferencesRepository.setGameExistedState(true)
            } returns DataResult.Success("")
            val savedStateHandle = SavedStateHandle(
                mapOf(GAME_LEVEL_SAVED_KEY to GameLevel.EASY.name)
            )
            val viewModel = GameViewModel(
                puzzleRepository, noteRepository, sudokuRepository, preferencesRepository,
                savedStateHandle
            )
            delay(magicNum)
            clearAllMocks()

            coEvery {
                sudokuRepository.checkBoardAnswer(puzzle.board)
            } returns true
            viewModel.submitAnswer()
            delay(magicNum)
            coVerify(exactly = 1) {
                sudokuRepository.checkBoardAnswer(puzzle.board)
            }
            confirmVerified(sudokuRepository)
        }
        Unit
    }

    @Test
    fun `delete game`() = runBlocking {
        launch(Dispatchers.Main) {
            coEvery {
                preferencesRepository.getGameExistedStateLatest()
            } returns DataResult.Success(false)
            coEvery {
                sudokuRepository.getPuzzleBoard(GameLevel.EASY)
            } returns board
            coEvery {
                preferencesRepository.setGameExistedState(true)
            } returns DataResult.Success("")
            val savedStateHandle = SavedStateHandle(
                mapOf(GAME_LEVEL_SAVED_KEY to GameLevel.EASY.name)
            )
            val viewModel = GameViewModel(
                puzzleRepository, noteRepository, sudokuRepository, preferencesRepository,
                savedStateHandle
            )
            delay(magicNum)
            clearAllMocks()

            coEvery {
                preferencesRepository.setGameExistedState(false)
            } returns DataResult.Success("")
            viewModel.deleteGame()
            delay(magicNum)
            coVerify(exactly = 1) {
                noteRepository.deleteAllNote()
                puzzleRepository.deletePuzzle()
                preferencesRepository.setGameExistedState(false)
            }
        }
        Unit
    }

    @Test
    fun `init success`() = runBlocking {
        launch(Dispatchers.Main) {
            // new game
            coEvery {
                preferencesRepository.getGameExistedStateLatest()
            } returns DataResult.Success(false)
            coEvery {
                sudokuRepository.getPuzzleBoard(GameLevel.EASY)
            } returns board
            coEvery {
                preferencesRepository.setGameExistedState(true)
            } returns DataResult.Success("")
            val savedStateHandle = SavedStateHandle(
                mapOf(GAME_LEVEL_SAVED_KEY to GameLevel.EASY.name)
            )
            var viewModel = GameViewModel(
                puzzleRepository, noteRepository, sudokuRepository, preferencesRepository,
                savedStateHandle
            )
            delay(magicNum)
            coVerify(exactly = 1) {
                preferencesRepository.getGameExistedStateLatest()
                sudokuRepository.getPuzzleBoard(GameLevel.EASY)
                noteRepository.insertNoteList(listOf(note))
                puzzleRepository.insertPuzzle(puzzle)
                preferencesRepository.setGameExistedState(true)
            }
            assertTrue(viewModel.gameUiState.value is GameUiState.Playing)
            confirmVerified(
                puzzleRepository, noteRepository, sudokuRepository, preferencesRepository
            )

            clearAllMocks()

            // continue game
            coEvery {
                preferencesRepository.getGameExistedStateLatest()
            } returns DataResult.Success(true)
            viewModel = GameViewModel(
                puzzleRepository, noteRepository, sudokuRepository, preferencesRepository,
                SavedStateHandle()
            )
            delay(magicNum)
            coVerify(exactly = 1) {
                preferencesRepository.getGameExistedStateLatest()
                noteRepository.getNoteList()
                puzzleRepository.getPuzzle()
            }
            assertTrue(viewModel.gameUiState.value is GameUiState.Playing)
            confirmVerified(
                puzzleRepository, noteRepository, sudokuRepository, preferencesRepository
            )
        }
        Unit
    }

    @Test
    fun `init error`() = runBlocking {
        launch(Dispatchers.Main) {
            // new game
            coEvery {
                preferencesRepository.getGameExistedStateLatest()
            } returns DataResult.Success(false)
            coEvery {
                sudokuRepository.getPuzzleBoard(GameLevel.EASY)
            } returns board
            coEvery {
                preferencesRepository.setGameExistedState(true)
            } returns DataResult.Error(DataResult.Error.Code.UNKNOWN)
            val savedStateHandle = SavedStateHandle(
                mapOf(GAME_LEVEL_SAVED_KEY to GameLevel.EASY.name)
            )
            var viewModel = GameViewModel(
                puzzleRepository, noteRepository, sudokuRepository, preferencesRepository,
                savedStateHandle
            )
            delay(magicNum)
            coVerify(exactly = 1) {
                preferencesRepository.getGameExistedStateLatest()
                sudokuRepository.getPuzzleBoard(GameLevel.EASY)
                noteRepository.insertNoteList(listOf(note))
                puzzleRepository.insertPuzzle(puzzle)
                preferencesRepository.setGameExistedState(true)
            }
            assertTrue(viewModel.gameUiState.value is GameUiState.Error)
            confirmVerified(
                puzzleRepository, noteRepository, sudokuRepository, preferencesRepository
            )

            clearAllMocks()

            // get game existed error
            coEvery {
                preferencesRepository.getGameExistedStateLatest()
            } returns DataResult.Error(DataResult.Error.Code.UNKNOWN)
            viewModel = GameViewModel(
                puzzleRepository, noteRepository, sudokuRepository, preferencesRepository,
                SavedStateHandle()
            )
            delay(magicNum)
            assertTrue(viewModel.gameUiState.value is GameUiState.Error)
        }
        Unit
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun clean() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }
}