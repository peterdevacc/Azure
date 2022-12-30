package com.peter.azure.data.util

import androidx.room.TypeConverter
import com.peter.azure.data.entity.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AzureTypeConverters {

    @TypeConverter
    fun fromBoard(board: List<List<Cell>>): String {
        return Json.encodeToString(board)
    }

    @TypeConverter
    fun stringToBoard(boardString: String): List<List<Cell>> {
        return Json.decodeFromString(boardString)
    }

    @TypeConverter
    fun fromMarkList(markList: List<Mark>): String {
        return Json.encodeToString(markList)
    }

    @TypeConverter
    fun stringToMarkList(markListString: String): List<Mark> {
        return Json.decodeFromString(markListString)
    }

    @TypeConverter
    fun fromLocation(location: Location): String {
        return Json.encodeToString(location)
    }

    @TypeConverter
    fun stringToLocation(locationString: String): Location {
        return Json.decodeFromString(locationString)
    }

}