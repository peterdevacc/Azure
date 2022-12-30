package com.peter.azure.data.entity

import android.graphics.Bitmap
import java.io.File

data class SudokuPdf(
    val file: File,
    val preview: List<Bitmap>
)
