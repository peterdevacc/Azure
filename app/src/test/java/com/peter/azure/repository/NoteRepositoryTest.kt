package com.peter.azure.repository

import com.peter.azure.data.dao.NoteDAO
import com.peter.azure.data.entity.Location
import com.peter.azure.data.entity.Mark
import com.peter.azure.data.entity.Note
import com.peter.azure.data.repository.NoteRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class NoteRepositoryTest {

    private val noteDAO = mockk<NoteDAO>(relaxed = true)
    private val noteRepository = NoteRepository(noteDAO)

    private val noteList = listOf(
        Note(Location(1, 1), Mark.getDefaultList().toMutableList())
    )

    @Test
    fun `get note list`() = runBlocking {
        noteRepository.getNoteList()

        coVerify(exactly = 1) {
            noteDAO.getNotes()
        }

        confirmVerified(noteDAO)
    }

    @Test
    fun `insert note list`() = runBlocking {
        noteRepository.insertNoteList(noteList)

        coVerify(exactly = 1) {
            noteDAO.insertNotes(noteList)
        }

        confirmVerified(noteDAO)
    }

    @Test
    fun `update note`() = runBlocking {
        noteRepository.updateNote(noteList.first())

        coVerify(exactly = 1) {
            noteDAO.updateNote(noteList.first())
        }

        confirmVerified(noteDAO)
    }

    @Test
    fun `delete all note`() = runBlocking {
        noteRepository.deleteAllNote()

        coVerify(exactly = 1) {
            noteDAO.deleteNotes()
        }

        confirmVerified(noteDAO)
    }

}