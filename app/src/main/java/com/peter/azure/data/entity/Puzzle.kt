package com.peter.azure.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "puzzle",
    primaryKeys = ["id"]
)
data class Puzzle(

    @ColumnInfo(name = "id")
    val id: Long = 1,

    @ColumnInfo(name = "board")
    val board: List<List<Cell>> = emptyList()

) {
    fun getCell(y: Int, x: Int): Cell {
        return board[y][x]
    }
}
