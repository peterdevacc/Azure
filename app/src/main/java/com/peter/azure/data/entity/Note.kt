package com.peter.azure.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "note",
    primaryKeys = ["location"]
)
data class Note(

    @ColumnInfo(name = "location")
    val location: Location,

    @ColumnInfo(name = "mark_list")
    val markList: List<Mark>

)
