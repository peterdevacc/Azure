package com.peter.azure.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.peter.azure.data.AzureDatabase
import com.peter.azure.data.dao.PuzzleDAO
import com.peter.azure.data.entity.Puzzle
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
class SavedPuzzleDAOTest {

//    private lateinit var database: AzureDatabase
//    private lateinit var dao: PuzzleDAO
//
//    private val puzzle = Puzzle(
//        0, ""
//    )
//
//    @Test
//    fun `game operation`() = runBlocking {
//
//        var result = dao.getSavedGame()
//        assertNull(result)
//
//        dao.upsertSavedGame(puzzle)
//        result = dao.getSavedGame()
//        puzzle.id = 1
//        assertEquals(puzzle, result)
//
//        dao.deleteSavedGame()
//        result = dao.getSavedGame()
//        assertNull(result)
//    }
//
//    @Before
//    fun setup() {
//        val context = InstrumentationRegistry.getInstrumentation().targetContext
//        database = Room.inMemoryDatabaseBuilder(
//            context,
//            AzureDatabase::class.java
//        ).build()
//        dao = database.getSavedGameDAO()
//    }
//
//    @After
//    fun clean() {
//        database.close()
//    }

}