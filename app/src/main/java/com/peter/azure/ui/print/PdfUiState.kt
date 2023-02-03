/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
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