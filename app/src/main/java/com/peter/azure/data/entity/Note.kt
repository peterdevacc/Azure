/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

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
