/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.home

import com.peter.azure.data.entity.GameLevel
import com.peter.azure.data.util.DataResult

sealed interface HomeUiState {

    data class Success(
        val gameExisted: Boolean,
        var gameLevel: GameLevel,
    ) : HomeUiState

    data class Error(
        val code: DataResult.Error.Code
    ) : HomeUiState

    object Loading : HomeUiState

}