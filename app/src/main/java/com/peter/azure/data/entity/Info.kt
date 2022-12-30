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
