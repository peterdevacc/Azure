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
