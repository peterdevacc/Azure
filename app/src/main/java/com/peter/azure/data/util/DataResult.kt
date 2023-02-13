/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.data.util

sealed interface DataResult<out T> {

    data class Success<T>(
        val result: T
    ) : DataResult<T>

    data class Error(
        val code: Code
    ) : DataResult<Nothing> {

        enum class Code {
            UNKNOWN,
            IO
        }

    }

}
