package com.peter.azure.data.entity


@kotlinx.serialization.Serializable
data class Cell(
    val num: Int,
    val type: Type,
) {
    enum class Type {
        QUESTION, BLANK
    }
}
