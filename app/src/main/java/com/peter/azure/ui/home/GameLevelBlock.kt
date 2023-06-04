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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.azure.data.entity.GameLevel
import com.peter.azure.ui.theme.AzureTheme

@Composable
fun GameLevelBlock(
    fullSize: Dp,
    gameLevel: GameLevel,
    semanticsDescription: String,
    anim: Float
) {
    val block = @Composable {
        Block(
            fullSize = fullSize,
            gameLevel = gameLevel,
            anim = anim
        )
    }

    Layout(
        contents = listOf(block),
        modifier = Modifier.clearAndSetSemantics {
            text = AnnotatedString(semanticsDescription)
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

@Composable
private fun Block(
    fullSize: Dp,
    gameLevel: GameLevel,
    anim: Float
) {

    val density = LocalDensity.current
    val fullSizePx = with(density) { fullSize.toPx() }
    val frameBrush = Brush.linearGradient(
        listOf(
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.primary,
        )
    )

    val strokeWidth = fullSize.value / 16f
    val lineGap = fullSizePx / 3f

    val numTextList = when (gameLevel) {
        GameLevel.EASY -> listOf(
            "1" to 0, "3" to 1, "5" to 3, "6" to 4, "4" to 5, "7" to 6, "2" to 8
        )

        GameLevel.MODERATE -> listOf(
            "8" to 0, "2" to 2, "9" to 5, "4" to 6, "5" to 7
        )

        GameLevel.HARD -> listOf(
            "2" to 0, "1" to 3, "8" to 8
        )
    }

    val puzzleBlockColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
    val textMeasurer = rememberTextMeasurer()
    val fontSize = (fullSize.value / 6).sp
    val textStyle = MaterialTheme.typography.headlineLarge.copy(
        fontSize = fontSize,
        color = MaterialTheme.colorScheme.onBackground
    )
    val numTextLayout: (String) -> TextLayoutResult = {
        textMeasurer.measure(
            text = AnnotatedString(it),
            style = textStyle,
        )
    }

    val firstCubicCenter = lineGap / 2f
    val numTextBackgroundLength = lineGap - strokeWidth
    val numTextDrawDataList = numTextList
        .map { (num, index) ->
            val numTextLayoutResult = numTextLayout(num)
            val numTextSize = numTextLayoutResult.size
            val indexRoundA = (index % 3)
            val indexRoundB = (index / 3)
            val numTextX = (indexRoundA * 2 + 1) * firstCubicCenter - numTextSize.width / 2f
            val numTextY = (indexRoundB * 2 + 1) * firstCubicCenter - numTextSize.height / 2f
            val backgroundX = strokeWidth + (lineGap * indexRoundA) - strokeWidth / 2f
            val backgroundY = strokeWidth + (lineGap * indexRoundB) - strokeWidth / 2f

            Triple(
                numTextLayoutResult,
                Offset(backgroundX, backgroundY),
                Offset(numTextX, numTextY)
            )
        }

    Canvas(
        modifier = Modifier.size(fullSize)
    ) {
        drawRect(
            brush = frameBrush,
            topLeft = Offset.Zero,
            size = Size(fullSizePx, fullSizePx),
            style = Stroke(width = strokeWidth),
            alpha = anim
        )

        for (i in 1..2) {
            val gapV = strokeWidth * 0.5f
            val gapH = lineGap * i
            val len = anim * (fullSizePx - strokeWidth * 0.5f)

            val startH = Offset(gapV, gapH)
            val endH = Offset(len, gapH)
            drawLine(
                brush = frameBrush,
                start = startH, end = endH,
                strokeWidth = strokeWidth
            )

            val startV = Offset(gapH, gapV)
            val endV = Offset(gapH, len)
            drawLine(
                brush = frameBrush,
                start = startV, end = endV,
                strokeWidth = strokeWidth
            )
        }

        numTextDrawDataList.forEach { (layoutResult, backgroundTopLeft, textTopLeft) ->
            drawRect(
                color = puzzleBlockColor,
                topLeft = Offset(backgroundTopLeft.x, backgroundTopLeft.y),
                size = Size(numTextBackgroundLength, numTextBackgroundLength),
                alpha = anim
            )

            drawText(
                textLayoutResult = layoutResult,
                topLeft = Offset(textTopLeft.x, textTopLeft.y),
                alpha = anim
            )
        }

    }

}

@Preview(
    name = "GameLevel Block",
    showBackground = true
)
@Preview(
    name = "GameLevel Block",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun GameLevelBlockPreview() {
    AzureTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            GameLevelBlock(256.dp, GameLevel.EASY, "", 1f)
        }
    }
}