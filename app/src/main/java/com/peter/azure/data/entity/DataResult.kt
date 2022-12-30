package com.peter.azure.data.entity

sealed interface DataResult<out T> {

    data class Success<T>(
        val result: T
    ): DataResult<T>

    data class Error(
        val code: Code
    ): DataResult<Nothing> {

        enum class Code {
            UNKNOWN,
            IO
        }

    }

}
