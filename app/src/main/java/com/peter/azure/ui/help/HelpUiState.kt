/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.help

import com.peter.azure.data.util.DataResult
import com.peter.azure.data.entity.Help

sealed interface HelpUiState {

    object Loading : HelpUiState

    data class Success(
        val helpMap: Map<Help.Catalog, List<Help>>
    ) : HelpUiState

    data class Error(
        val code: DataResult.Error.Code
    ) : HelpUiState

}