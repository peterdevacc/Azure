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