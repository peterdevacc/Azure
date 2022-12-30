package com.peter.azure.ui.greeting

import com.peter.azure.data.entity.DataResult
import com.peter.azure.data.entity.Info

sealed interface GreetingUiState {

    data class ContractDialogLoaded(
        val info: Info
    ): GreetingUiState

    object Processing: GreetingUiState

    object ContractsAgreed: GreetingUiState

    data class Error(
        val code: DataResult.Error.Code
    ): GreetingUiState

    object Default: GreetingUiState

}