package com.peter.azure.repository

import com.peter.azure.data.entity.Cell
import com.peter.azure.data.entity.GameLevel
import com.peter.azure.data.entity.Puzzle
import com.peter.azure.data.repository.SudokuRepository
import com.peter.common.Sudoku
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SudokuRepositoryTest {

    private val sudoku = mockk<Sudoku>(relaxed = true)
    private val sudokuRepository = SudokuRepository(sudoku)

    private val gameLevelList = listOf(
        GameLevel.MODERATE, GameLevel.EASY
    )
    private val puzzle = Puzzle(
        board = listOf(
            listOf(
                Cell(0, Cell.Type.BLANK), Cell(5, Cell.Type.QUESTION)
            )
        )
    )

    @Test
    fun `get board`() = runBlocking {
        sudokuRepository.getPuzzleBoard(gameLevelList.first())

        coVerify(exactly = 1) {
            sudoku.createSudoku(gameLevelList.first().num)
        }

        confirmVerified(sudoku)
    }

    @Test
    fun `get print game list`() = runBlocking {
        sudokuRepository.getPrintGameList(gameLevelList)

        gameLevelList.forEach {
            coVerify(exactly = 1) {
                sudoku.createSudoku(it.num)
            }
        }

        confirmVerified(sudoku)
    }

    @Test
    fun `check board answer`() = runBlocking {
        sudokuRepository.checkBoardAnswer(puzzle.board)

        coVerify(exactly = 1) {
            sudoku.verifySudokuAnswer(
                puzzle.board.map { row ->
                    row.map {
                        it.num
                    }
                }
            )
        }

        confirmVerified(sudoku)
    }

}