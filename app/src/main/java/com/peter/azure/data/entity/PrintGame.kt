/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.data.entity

data class PrintGame(
    val gameLevel: GameLevel,
    val sudoku: List<List<Int>>
)
