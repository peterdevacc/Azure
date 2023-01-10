/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.data.repository

import com.peter.azure.data.dao.NoteDAO
import com.peter.azure.data.entity.Note
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDAO: NoteDAO
) {

    suspend fun getNoteList() =
        noteDAO.getNotes()

    suspend fun insertNoteList(noteList: List<Note>) =
        noteDAO.insertNotes(noteList)

    suspend fun updateNote(note: Note) =
        noteDAO.updateNote(note)

    suspend fun deleteAllNote() =
        noteDAO.deleteNotes()

}