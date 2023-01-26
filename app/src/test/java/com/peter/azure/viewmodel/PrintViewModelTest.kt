package com.peter.azure.viewmodel

import com.peter.azure.data.entity.*
import com.peter.azure.data.repository.PdfRepository
import com.peter.azure.data.repository.SudokuRepository
import com.peter.azure.data.util.DataResult
import com.peter.azure.ui.print.PdfUiState
import com.peter.azure.ui.print.PrintViewModel
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

class PrintViewModelTest {

    private val sudokuRepository = mockk<SudokuRepository>(relaxed = true)
    private val pdfRepository = mockk<PdfRepository>(relaxed = true)
    private val viewModel = PrintViewModel(
        sudokuRepository, pdfRepository
    )

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val magicNum = 600L

    private val gameLevelList = listOf(
        GameLevel.MODERATE, GameLevel.EASY, GameLevel.MODERATE, GameLevel.EASY,
        GameLevel.HARD, GameLevel.HARD
    )

    @Test
    fun `add game level`() = runBlocking {
        gameLevelList.forEach {
            viewModel.addGameLevel(it)
        }

        assertEquals(5, viewModel.gameLevelList.value.size)
    }

    @Test
    fun `remove game level`() = runBlocking {
        gameLevelList.forEach {
            viewModel.removeGameLevel(it)
        }

        assertTrue(viewModel.gameLevelList.value.isEmpty())
    }

    @Test
    fun `generate Sudoku PDF`() = runBlocking {
        launch(Dispatchers.Main) {
            // EmptyGameLevelList
            viewModel.generateSudokuPdf()
            delay(magicNum)
            coVerify(exactly = 0) {
                sudokuRepository.getPrintGameList(viewModel.gameLevelList.value)
                pdfRepository.generateSudokuPdf(any())
            }
            confirmVerified(sudokuRepository, pdfRepository)
            assertTrue(viewModel.pdfUiState.value is PdfUiState.EmptyGameLevelList)

            clearAllMocks()

            // Loaded
            coEvery {
                sudokuRepository.getPrintGameList(
                    viewModel.gameLevelList.value
                )
            } returns emptyList()
            val sudokuPdf = SudokuPdf(File(""), emptyList())
            coEvery {
                pdfRepository.generateSudokuPdf(emptyList())
            } returns DataResult.Success(sudokuPdf)
            viewModel.addGameLevel(GameLevel.EASY)
            viewModel.generateSudokuPdf()
            delay(magicNum)
            coVerify(exactly = 1) {
                sudokuRepository.getPrintGameList(viewModel.gameLevelList.value)
                pdfRepository.generateSudokuPdf(emptyList())
            }
            confirmVerified(sudokuRepository, pdfRepository)
            assertTrue(viewModel.pdfUiState.value is PdfUiState.Loaded)
            val resultSuccess = (viewModel.pdfUiState.value as PdfUiState.Loaded).sudokuPdf
            assertEquals(sudokuPdf, resultSuccess)

            clearAllMocks()

            // Error
            coEvery {
                sudokuRepository.getPrintGameList(
                    viewModel.gameLevelList.value
                )
            } returns emptyList()
            coEvery {
                pdfRepository.generateSudokuPdf(emptyList())
            } returns DataResult.Error(DataResult.Error.Code.UNKNOWN)
            viewModel.addGameLevel(GameLevel.EASY)
            viewModel.generateSudokuPdf()
            delay(magicNum)
            coVerify(exactly = 1) {
                sudokuRepository.getPrintGameList(viewModel.gameLevelList.value)
                pdfRepository.generateSudokuPdf(emptyList())
            }
            confirmVerified(sudokuRepository, pdfRepository)
            assertTrue(viewModel.pdfUiState.value is PdfUiState.Error)
            val resultError = (viewModel.pdfUiState.value as PdfUiState.Error).code
            assertEquals(DataResult.Error.Code.UNKNOWN, resultError)
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