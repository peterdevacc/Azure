package com.peter.azure.data.entity

data class PrintGame(
    val gameLevel: GameLevel,
    val sudoku: List<List<Int>>
)
