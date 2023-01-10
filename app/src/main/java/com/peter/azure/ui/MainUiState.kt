/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui

import com.peter.azure.data.entity.DataResult

sealed interface MainUiState {

    data class Success(
        val startDestination: String
    ): MainUiState

    data class Error(
        val code: DataResult.Error.Code
    ): MainUiState

    object Loading: MainUiState

}
