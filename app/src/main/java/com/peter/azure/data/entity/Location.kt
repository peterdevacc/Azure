/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
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
