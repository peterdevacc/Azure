package com.peter.azure.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.peter.azure.data.entity.GameLevel
import com.peter.azure.data.repository.PdfRepository
import com.peter.azure.data.repository.SudokuRepository
import com.peter.azure.data.util.DataResult
import com.peter.azure.data.util.PDF_NAME_PREFIX
import com.peter.common.Sudoku
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PdfRepositoryTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val pdfRepository = PdfRepository(context)

    private val sudoku = Sudoku()
    private val sudokuRepository = SudokuRepository(sudoku)
    private val gameLevelList = listOf(GameLevel.EASY, GameLevel.MODERATE, GameLevel.HARD)
    private val appName = "AzureNum"
    private val gameLevelTitlePrefix = "Game Level"
    private val gameLevelTextList = listOf(
        "Easy",
        "Moderate",
        "Hard",
    )

    @Test
    fun generateSudokuPdfTest() = runBlocking {
        val result = pdfRepository.createSudokuPdf(
            appName = appName,
            gameLevelTitlePrefix = gameLevelTitlePrefix,
            gameLevelTextList = gameLevelTextList,
            printGameList = sudokuRepository.getPrintGameList(gameLevelList)
        )
        assertTrue(result is DataResult.Success)
        val sudokuPdf = (result as DataResult.Success).result
        assertTrue(sudokuPdf.file.length() > 0)
        assertTrue(sudokuPdf.preview.size == gameLevelList.size)
        sudokuPdf.preview.forEach {
            assertTrue(it.allocationByteCount > 0)
        }

        pdfRepository.createSudokuPdf(
            appName = appName,
            gameLevelTitlePrefix = gameLevelTitlePrefix,
            gameLevelTextList = gameLevelTextList,
            printGameList = sudokuRepository.getPrintGameList(
                listOf(gameLevelList.first())
            )
        )
        pdfRepository.createSudokuPdf(
            appName = appName,
            gameLevelTitlePrefix = gameLevelTitlePrefix,
            gameLevelTextList = gameLevelTextList,
            printGameList = sudokuRepository.getPrintGameList(
                listOf(gameLevelList[1])
            )
        )
        pdfRepository.createSudokuPdf(
            appName = appName,
            gameLevelTitlePrefix = gameLevelTitlePrefix,
            gameLevelTextList = gameLevelTextList,
            printGameList = sudokuRepository.getPrintGameList(
                listOf(gameLevelList[2])
            )
        )
        pdfRepository.createSudokuPdf(
            appName = appName,
            gameLevelTitlePrefix = gameLevelTitlePrefix,
            gameLevelTextList = gameLevelTextList,
            printGameList = sudokuRepository.getPrintGameList(
                listOf(gameLevelList.first(), gameLevelList[1])
            )
        )
        pdfRepository.createSudokuPdf(
            appName = appName,
            gameLevelTitlePrefix = gameLevelTitlePrefix,
            gameLevelTextList = gameLevelTextList,
            printGameList = sudokuRepository.getPrintGameList(
                listOf(gameLevelList[2], gameLevelList[1])
            )
        )

        val fileList = context.filesDir.listFiles()
        assertNotNull(fileList)
        val pdfList = fileList!!.filter {
            it.name.startsWith(PDF_NAME_PREFIX)
        }
        assertEquals(2, pdfList.size)

    }

    @Test
    fun deleteCachePdfTest() = runBlocking {
        pdfRepository.createSudokuPdf(
            appName = appName,
            gameLevelTitlePrefix = gameLevelTitlePrefix,
            gameLevelTextList = gameLevelTextList,
            printGameList = sudokuRepository.getPrintGameList(gameLevelList)
        )
        pdfRepository.deleteCachePDF()

        val fileList = context.filesDir.listFiles()
        assertNotNull(fileList)
        val pdfList = fileList!!.filter {
            it.name.startsWith(PDF_NAME_PREFIX)
        }
        assertTrue(pdfList.isEmpty())
    }

    @Before
    fun setup() {
        val fileList = context.filesDir.listFiles()
        assertNotNull(fileList)
        val pdfList = fileList!!.filter {
            it.name.startsWith(PDF_NAME_PREFIX)
        }
        pdfList.forEach {
            it.delete()
        }
    }

}