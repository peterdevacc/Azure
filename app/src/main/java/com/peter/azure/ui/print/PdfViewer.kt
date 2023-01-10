/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.print

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlin.math.sqrt

@Composable
fun PdfViewer(
    modifier: Modifier = Modifier,
    preview: List<Bitmap>,
) {
    val pageCount = preview.size
    val context = LocalContext.current
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val width = with(LocalDensity.current) { maxWidth.toPx() }.toInt()
        val height = (width * sqrt(2f)).toInt()
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            itemsIndexed(preview) { index, bitmap ->
                val request = ImageRequest.Builder(context)
                    .size(width, height)
                    .data(bitmap)
                    .build()
                AsyncImage(
                    model = request,
                    contentDescription = "Sudoku Page ${index + 1} of $pageCount",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .background(Color.White)
                        .aspectRatio(1f / sqrt(2f))
                        .fillMaxWidth(),
                )
            }
        }
    }
}
