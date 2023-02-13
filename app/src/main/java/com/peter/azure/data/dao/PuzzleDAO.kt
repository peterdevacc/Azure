/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.data.dao

import androidx.room.*
import com.peter.azure.data.entity.Puzzle

@Dao
interface PuzzleDAO {

    @Query("SELECT * FROM puzzle WHERE id = 1")
    suspend fun getPuzzle(): Puzzle

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPuzzle(puzzle: Puzzle)

    @Update
    suspend fun updatePuzzle(puzzle: Puzzle)

    @Query("DELETE FROM puzzle")
    suspend fun deletePuzzle()

}