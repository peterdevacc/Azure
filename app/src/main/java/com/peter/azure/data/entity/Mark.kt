/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.data.entity

@kotlinx.serialization.Serializable
enum class Mark {
    Potential, WRONG, NONE;

    companion object {
        fun getDefaultList(): List<Mark> {
            return listOf(
                NONE, NONE, NONE,
                NONE, NONE, NONE,
                NONE, NONE, NONE
            )
        }
    }
}