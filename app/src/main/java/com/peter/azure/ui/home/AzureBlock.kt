/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.azure.R
import com.peter.azure.ui.theme.AzureTheme

@Composable
fun AzureBlock(fullSize: Dp) {

    val block = @Composable {
        Block(fullSize = fullSize)
    }

    val description = stringResource(R.string.game_existed_description)

    Layout(
        contents = listOf(block),
        modifier = Modifier.clearAndSetSemantics {
            text = AnnotatedString(description)
        }
    ) { (blockMeasure), constraints ->

        val blockPlaceable = blockMeasure.first().measure(constraints)
        val totalHeight = blockPlaceable.height
        val totalWidth = blockPlaceable.width

        layout(totalWidth, totalHeight) {
            blockPlaceable.place(0, 0, 1f)
        }

    }

}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun Block(fullSize: Dp) {

    val density = LocalDensity.current
    val fullSizePx = with(density) { fullSize.toPx() }
    val frameBrush = Brush.linearGradient(
        listOf(
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.primary,
        )
    )
    val markColor = MaterialTheme.colorScheme.secondary

    val strokeWidth = fullSize.value / 16f
    val lineGap = fullSizePx / 3

    val potentialMark = rememberVectorPainter(
        ImageVector.vectorResource(R.drawable.ic_mark_potential_24)
    )
    val wrongMark = rememberVectorPainter(
        ImageVector.vectorResource(R.drawable.ic_mark_wrong_24)
    )
    val noneMark = rememberVectorPainter(
        ImageVector.vectorResource(R.drawable.ic_mark_none_24)
    )
    val markSize = fullSizePx / 4.5f

    val textMeasurer = rememberTextMeasurer()
    val fontSize = (fullSize.value / 6).sp
    val textStyle = MaterialTheme.typography.headlineLarge.copy(
        fontSize = fontSize
    )
    val numTextLayout: (String) -> TextLayoutResult = {
        textMeasurer.measure(
            text = AnnotatedString(it),
            style = textStyle,
        )
    }

    val numTwoTextLayoutResult = numTextLayout("2")
    val numTwoTextSize = numTwoTextLayoutResult.size
    val numTwoColor = MaterialTheme.colorScheme.onBackground

    val numEightTextLayoutResult = numTextLayout("8")
    val numEightTextSize = numEightTextLayoutResult.size
    val numEightColor = MaterialTheme.colorScheme.tertiary

    val numSevenTextLayoutResult = numTextLayout("7")
    val numSevenTextSize = numSevenTextLayoutResult.size
    val numSevenColor = MaterialTheme.colorScheme.primary

    val firstCubicCenter = lineGap / 2f
    val secondCubicCenter = fullSizePx / 2f
    val thirdCubicCenter = fullSizePx - ((fullSizePx - (lineGap * 2f)) / 2f)

    Canvas(
        modifier = Modifier.size(fullSize)
    ) {
        drawRect(
            brush = frameBrush,
            topLeft = Offset.Zero,
            size = Size(fullSizePx, fullSizePx),
            style = Stroke(width = strokeWidth)
        )

        for (i in 1..2) {
            val start = Offset(strokeWidth * 0.5f, lineGap * i)
            val end = Offset(fullSizePx - strokeWidth * 0.5f, lineGap * i)
            drawLine(
                brush = frameBrush,
                start = start, end = end,
                strokeWidth = strokeWidth
            )
        }

        for (i in 1..2) {
            val start = Offset(lineGap * i, strokeWidth * 0.5f)
            val end = Offset(lineGap * i, fullSizePx - strokeWidth * 0.5f)
            drawLine(
                brush = frameBrush,
                start = start, end = end,
                strokeWidth = strokeWidth
            )
        }

        drawText(
            textLayoutResult = numTwoTextLayoutResult,
            color = numTwoColor,
            topLeft = Offset(
                firstCubicCenter - (numTwoTextSize.width / 2f),
                firstCubicCenter - (numTwoTextSize.height / 2f)
            ),
        )

        drawText(
            textLayoutResult = numEightTextLayoutResult,
            color = numEightColor,
            topLeft = Offset(
                firstCubicCenter - (numEightTextSize.width / 2f),
                secondCubicCenter - (numEightTextSize.height / 2f)
            ),
        )

        drawText(
            textLayoutResult = numSevenTextLayoutResult,
            color = numSevenColor,
            topLeft = Offset(
                thirdCubicCenter - (numSevenTextSize.width / 2f),
                firstCubicCenter - (numSevenTextSize.height / 2f)
            ),
        )

        translate(
            left = secondCubicCenter - (markSize / 2f),
            top = secondCubicCenter - (markSize / 2f)
        ) {
            with(potentialMark) {
                draw(
                    size = Size(markSize, markSize),
                    colorFilter = ColorFilter.tint(markColor)
                )
            }
        }

        translate(
            left = secondCubicCenter - (markSize / 2f),
            top = thirdCubicCenter - (markSize / 2f)
        ) {
            with(wrongMark) {
                draw(
                    size = Size(markSize, markSize),
                    colorFilter = ColorFilter.tint(markColor)
                )
            }
        }

        translate(
            left = thirdCubicCenter - (markSize / 2f),
            top = thirdCubicCenter - (markSize / 2f)
        ) {
            with(noneMark) {
                draw(
                    size = Size(markSize, markSize),
                    colorFilter = ColorFilter.tint(markColor)
                )
            }
        }

    }

}

@Preview(name = "NumBlock", showBackground = true)
@Preview(name = "NumBlock", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AzureBlock() {
    AzureTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AzureBlock(192.dp)
        }
    }
}
