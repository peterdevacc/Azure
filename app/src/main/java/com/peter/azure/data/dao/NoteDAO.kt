/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.data.dao

import androidx.room.*
import com.peter.azure.data.entity.Note

@Dao
interface NoteDAO {

    @Query("SELECT * FROM note")
    suspend fun getNotes(): List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(noteList: List<Note>)

    @Update
    suspend fun updateNote(note: Note)

    @Query("DELETE FROM note")
    suspend fun deleteNotes()

}