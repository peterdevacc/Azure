/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.data.entity

@kotlinx.serialization.Serializable
data class Location(
    val x: Int,
    val y: Int,
) {
    fun isNotDefault(): Boolean {
        return x != -1 && y != -1
    }

    companion object {
        fun getDefault(): Location {
            return Location(-1, -1)
        }
    }
}
