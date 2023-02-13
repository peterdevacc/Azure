/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.data.entity

@kotlinx.serialization.Serializable
data class Location(
    val y: Int,
    val x: Int
) {
    fun isNotDefault(): Boolean {
        return y != -1 && x != -1
    }

    companion object {
        fun getDefault(): Location {
            return Location(-1, -1)
        }
    }
}
