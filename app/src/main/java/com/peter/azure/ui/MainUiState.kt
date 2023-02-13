/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui

import com.peter.azure.data.util.DataResult

sealed interface MainUiState {

    data class Success(
        val startDestination: String
    ) : MainUiState

    data class Error(
        val code: DataResult.Error.Code
    ) : MainUiState

    object Loading : MainUiState

}
