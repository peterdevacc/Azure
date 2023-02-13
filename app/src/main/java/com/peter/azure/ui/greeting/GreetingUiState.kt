/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.greeting

import com.peter.azure.data.entity.Info
import com.peter.azure.data.util.DataResult

sealed interface GreetingUiState {

    data class InfoDialogLoaded(
        val info: Info
    ) : GreetingUiState

    object Processing : GreetingUiState

    object InfoAccepted : GreetingUiState

    data class Error(
        val code: DataResult.Error.Code
    ) : GreetingUiState

    object Default : GreetingUiState

}