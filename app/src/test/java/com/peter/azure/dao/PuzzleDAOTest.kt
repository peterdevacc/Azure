package com.peter.azure.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.peter.azure.data.AzureDatabase
import com.peter.azure.data.dao.PuzzleDAO
import com.peter.azure.data.entity.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class PuzzleDAOTest {

    private lateinit var database: AzureDatabase
    private lateinit var dao: PuzzleDAO

    private val cellList = mutableListOf(
        Cell(0, Cell.Type.BLANK), Cell(5, Cell.Type.QUESTION)
    )
    private val puzzle = Puzzle(
        board = mutableListOf(cellList)
    )

    @Test
    fun `puzzle operation`() = runBlocking {
        dao.insertPuzzle(puzzle)

        val resultBoard = dao.getPuzzle().board
        val resultCellList = resultBoard.first()
        assertEquals(cellList.size, resultCellList.size)
        repeat(cellList.size) {
            assertEquals(cellList[it].num, resultCellList[it].num)
            assertEquals(cellList[it].type, resultCellList[it].type)
        }

        val updatedCell = cellList.first().copy(num = 1)
        val updatedBoard = resultBoard.map { it.toMutableList() }
        updatedBoard[0][0] = updatedCell
        dao.updatePuzzle(
            puzzle.copy(board = updatedBoard)
        )

        val resultUpdated = dao.getPuzzle().board.first().first()
        assertEquals(updatedCell.num, resultUpdated.num)

        dao.deletePuzzle()
        val resultDeleted = dao.getPuzzle()
        assertNull(resultDeleted)
    }

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(
            context,
            AzureDatabase::class.java
        ).build()
        dao = database.getPuzzleDAO()
    }

    @After
    fun clean() {
        database.close()
    }

}