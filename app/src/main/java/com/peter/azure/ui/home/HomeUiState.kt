package com.peter.azure.ui.home

import com.peter.azure.data.entity.DataResult
import com.peter.azure.data.entity.GameLevel

sealed interface HomeUiState {

    data class Success(
        val gameExisted: Boolean,
        val gameLevel: GameLevel
    ): HomeUiState

    data class Error(
        val code: DataResult.Error.Code
    ): HomeUiState

    object Loading: HomeUiState

}