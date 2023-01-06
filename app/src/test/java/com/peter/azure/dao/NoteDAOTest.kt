package com.peter.azure.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.peter.azure.data.AzureDatabase
import com.peter.azure.data.dao.NoteDAO
import com.peter.azure.data.entity.Location
import com.peter.azure.data.entity.Mark
import com.peter.azure.data.entity.Note
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class NoteDAOTest {

    private lateinit var database: AzureDatabase
    private lateinit var dao: NoteDAO

    private val noteList = mutableListOf<Note>()
    private val size = 10

    @Test
    fun `note operation`() = runBlocking {
        dao.insertNotes(noteList)

        val resultNoteList = dao.getNotes()
        assertEquals(noteList.size, resultNoteList.size)
        repeat(size) {
            checkNote(noteList[it], resultNoteList[it])
        }

        val updatedMarkList = Mark.getDefaultList().toMutableList()
        updatedMarkList[0] = Mark.WRONG
        dao.updateNote(
            resultNoteList.first().copy(
                markList = updatedMarkList
            )
        )

        val resultNote = dao.getNotes().first()
        assertEquals(updatedMarkList[0], resultNote.markList.first())

        dao.deleteNotes()
        val resultDeleted = dao.getNotes()
        assertEquals(0, resultDeleted.size)
    }

    private fun checkNote(expected: Note, actual: Note) {
        assertEquals(expected.location, actual.location)
        assertEquals(expected.markList, actual.markList)
    }

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(
            context,
            AzureDatabase::class.java
        ).build()
        dao = database.getNoteDAO()

        repeat(size) {
            noteList.add(
                Note(Location(it, it), Mark.getDefaultList().toMutableList())
            )
        }
    }

    @After
    fun clean() {
        database.close()
    }

}