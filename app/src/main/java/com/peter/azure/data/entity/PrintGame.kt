/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.data.entity

data class PrintGame(
    val gameLevel: GameLevel,
    val sudoku: List<List<Int>>
)
