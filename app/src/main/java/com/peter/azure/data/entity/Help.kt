/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.data.entity

@kotlinx.serialization.Serializable
data class Help(
    val catalog: Catalog,
    val title: String,
    val text: String
) {
    enum class Catalog {
        FAQ, TUTORIAL
    }
}
