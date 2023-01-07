package com.peter.azure.repository

import androidx.test.platform.app.InstrumentationRegistry
import com.peter.azure.data.entity.DataResult
import com.peter.azure.data.entity.GameLevel
import com.peter.azure.data.repository.PdfRepository
import com.peter.azure.data.repository.SudokuRepository
import com.peter.common.Sudoku
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class PdfRepositoryTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val pdfRepository = PdfRepository(context)

    private val sudoku = Sudoku()
    private val sudokuRepository = SudokuRepository(sudoku)
    private val gameLevelList = listOf(GameLevel.EASY, GameLevel.MODERATE, GameLevel.HARD)

    @Test
    fun `generate Sudoku PDF`() = runBlocking {
        val result = pdfRepository.generateSudokuPdf(
            sudokuRepository.getPrintGameList(gameLevelList)
        )
        assertTrue(result is DataResult.Success)
        val sudokuPdf = (result as DataResult.Success).result
        assertTrue(sudokuPdf.file.length() > 0)
        assertTrue(sudokuPdf.preview.size == gameLevelList.size)
        sudokuPdf.preview.forEach {
            assertTrue(it.allocationByteCount > 0)
        }
    }

}