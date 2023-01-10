/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.data.repository

import com.peter.azure.data.entity.GameLevel
import com.peter.azure.data.entity.PrintGame
import com.peter.azure.data.entity.Puzzle
import com.peter.common.Sudoku
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SudokuRepository @Inject constructor(
    private val sudoku: Sudoku
) {

    suspend fun getBoard(gameLevel: GameLevel): List<List<Int>> =
        withContext(Dispatchers.Default) {
            return@withContext sudoku.createSudoku(gameLevel.num)
        }

    suspend fun getPrintGameList(gameLevelList: List<GameLevel>): List<PrintGame> =
        withContext(Dispatchers.Default) {
            val printGameList = mutableListOf<PrintGame>()
            gameLevelList.forEach { level ->
                printGameList.add(
                    PrintGame(level, sudoku.createSudoku(level.num))
                )
            }

            return@withContext printGameList.toList()
        }

    suspend fun checkAnswer(puzzle: Puzzle): Boolean =
        withContext(Dispatchers.Default) {
            return@withContext sudoku.verifySudokuAnswer(
                puzzle.board.map { row ->
                    row.map {
                        it.num
                    }
                }
            )
        }

}