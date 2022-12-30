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