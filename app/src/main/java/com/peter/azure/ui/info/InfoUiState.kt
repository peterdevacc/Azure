/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.info

import com.peter.azure.data.entity.Info
import com.peter.azure.data.util.DataResult

sealed interface InfoUiState {

    object Loading : InfoUiState

    data class Success(
        val info: Info
    ) : InfoUiState

    data class Error(
        val code: DataResult.Error.Code
    ) : InfoUiState

}