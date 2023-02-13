/*
 * Copyright (c) 2023 洪振健 All rights reserved.
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