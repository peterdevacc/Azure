/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.data.repository

import com.peter.azure.data.dao.PuzzleDAO
import com.peter.azure.data.entity.Puzzle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PuzzleRepository @Inject constructor(
    private val puzzleDAO: PuzzleDAO
) {

    suspend fun getPuzzle() =
        puzzleDAO.getPuzzle()

    suspend fun insertPuzzle(puzzle: Puzzle) =
        puzzleDAO.insertPuzzle(puzzle)

    suspend fun updatePuzzle(puzzle: Puzzle) =
        puzzleDAO.updatePuzzle(puzzle)

    suspend fun deletePuzzle() =
        puzzleDAO.deletePuzzle()

}