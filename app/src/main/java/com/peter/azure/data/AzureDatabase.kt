/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peter.azure.data.dao.NoteDAO
import com.peter.azure.data.dao.PuzzleDAO
import com.peter.azure.data.entity.Note
import com.peter.azure.data.entity.Puzzle
import com.peter.azure.data.util.AzureTypeConverters

@Database(
    entities = [Puzzle::class, Note::class],
    exportSchema = false,
    version = 1
)
@TypeConverters(AzureTypeConverters::class)
abstract class AzureDatabase : RoomDatabase() {

    abstract fun getPuzzleDAO(): PuzzleDAO
    abstract fun getNoteDAO(): NoteDAO

}