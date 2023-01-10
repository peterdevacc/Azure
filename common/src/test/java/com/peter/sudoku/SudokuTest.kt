package com.peter.sudoku

import com.peter.common.Sudoku
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SudokuTest {

    private val sudoku = Sudoku()

    @Test
    fun `create puzzle test`() {
        // GAME LEVEL: Easy = 0, Moderate = 1, Hard = 2
        val gameConfigList = listOf(
            0 to (4..5),
            1 to (5..6),
            2 to (6..7),
        )

        gameConfigList.forEach { config ->
            val puzzleEasy = sudoku.createSudoku(config.first)
            puzzleEasy.forEach { row ->
                val blankList = row.filter { num -> num == 0 }
                assertTrue(blankList.size in config.second)
            }
        }
    }

    @Test
    fun `verify answer`() {
        val jsonString = javaClass
            .getResource("/puzzle_answer.json")!!
            .readText()
        val board = Json.decodeFromString<List<List<Int>>>(jsonString)

        val resultCorrect = sudoku.verifySudokuAnswer(board)
        assertTrue(resultCorrect)

        val boardWrong = board.map { it.toMutableList() }
        boardWrong[0][0] = 0
        val resultWrong = sudoku.verifySudokuAnswer(boardWrong)
        assertFalse(resultWrong)
    }

}