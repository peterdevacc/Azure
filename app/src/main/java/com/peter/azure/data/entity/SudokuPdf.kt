/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.data.entity

import android.graphics.Bitmap
import java.io.File

data class SudokuPdf(
    val file: File,
    val preview: List<Bitmap>
)
