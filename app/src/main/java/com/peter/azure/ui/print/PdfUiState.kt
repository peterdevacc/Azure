/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.print

import com.peter.azure.data.entity.SudokuPdf
import com.peter.azure.data.util.DataResult

sealed interface PdfUiState {

    data class Loaded(
        val sudokuPdf: SudokuPdf
    ) : PdfUiState

    object Processing : PdfUiState

    object EmptyGameLevelList : PdfUiState

    object NotShareable : PdfUiState

    object Default : PdfUiState

    data class Error(
        val code: DataResult.Error.Code
    ) : PdfUiState

}