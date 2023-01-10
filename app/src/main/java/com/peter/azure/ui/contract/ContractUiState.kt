/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.contract

import com.peter.azure.data.entity.DataResult
import com.peter.azure.data.entity.Info

sealed interface ContractUiState {

    object Loading: ContractUiState

    data class Success(
        val info: Info
    ): ContractUiState

    data class Error(
        val code: DataResult.Error.Code
    ): ContractUiState

}