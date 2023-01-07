package com.peter.azure.repository

import com.peter.azure.data.dao.PuzzleDAO
import com.peter.azure.data.entity.*
import com.peter.azure.data.repository.PuzzleRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class PuzzleRepositoryTest {

    private val puzzleDAO = mockk<PuzzleDAO>(relaxed = true)
    private val puzzleRepository = PuzzleRepository(puzzleDAO)

    private val puzzle = Puzzle(
        board = listOf(
            listOf(
                Cell(0, Cell.Type.BLANK), Cell(5, Cell.Type.QUESTION)
            )
        )
    )

    @Test
    fun `get puzzle`() = runBlocking {
        puzzleRepository.getPuzzle()

        coVerify(exactly = 1) {
            puzzleDAO.getPuzzle()
        }

        confirmVerified(puzzleDAO)
    }

    @Test
    fun `insert puzzle`() = runBlocking {
        puzzleRepository.insertPuzzle(puzzle)

        coVerify(exactly = 1) {
            puzzleDAO.insertPuzzle(puzzle)
        }

        confirmVerified(puzzleDAO)
    }

    @Test
    fun `update puzzle`() = runBlocking {
        puzzleRepository.updatePuzzle(puzzle)

        coVerify(exactly = 1) {
            puzzleDAO.updatePuzzle(puzzle)
        }

        confirmVerified(puzzleDAO)
    }

    @Test
    fun `delete puzzle`() = runBlocking {
        puzzleRepository.deletePuzzle()

        coVerify(exactly = 1) {
            puzzleDAO.deletePuzzle()
        }

        confirmVerified(puzzleDAO)
    }

}