/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.home

import com.peter.azure.data.entity.DataResult

sealed interface HomeUiState {

    data class Success(
        val gameExisted: Boolean,
        var dialAngle: Double,
    ) : HomeUiState

    data class Error(
        val code: DataResult.Error.Code
    ) : HomeUiState

    object Loading : HomeUiState

}