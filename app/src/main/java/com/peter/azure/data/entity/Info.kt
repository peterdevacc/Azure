/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.data.entity

@kotlinx.serialization.Serializable
data class Info(
    val type: Type,
    val data: List<Item>
) {

    enum class Type {
        SERVICE, PRIVACY, ACKNOWLEDGEMENTS;

        fun getFileName(): String {
            return "${name.lowercase()}.json"
        }
    }

    @kotlinx.serialization.Serializable
    data class Item(
        val type: Type,
        val text: String,
    ) {
        enum class Type {
            TITLE,
            TEXT,
            SIGNATURE
        }
    }

}
