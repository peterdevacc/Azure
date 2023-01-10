/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
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
