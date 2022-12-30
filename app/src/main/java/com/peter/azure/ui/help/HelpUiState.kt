package com.peter.azure.ui.help

import com.peter.azure.data.entity.DataResult
import com.peter.azure.data.entity.Help

sealed interface HelpUiState {

    object Loading: HelpUiState

    data class Success(
        val helpMap: Map<Help.Catalog, List<Help>>
    ): HelpUiState

    data class Error(
        val code: DataResult.Error.Code
    ): HelpUiState

}