/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.data.entity

@kotlinx.serialization.Serializable
enum class Mark {
    POTENTIAL, WRONG, NONE;

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