/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
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