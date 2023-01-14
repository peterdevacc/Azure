/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.data.entity

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
