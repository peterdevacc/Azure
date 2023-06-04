/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.home

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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

@Composable
private fun Block(fullSize: Dp) {

    val density = LocalDensity.current
    val fullSizePx = with(density) { fullSize.toPx() }

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
    val markSize = fullSizePx / 4.8f
    val potentialMarkColor = MaterialTheme.colorScheme.secondary
    val wrongMarkColor = MaterialTheme.colorScheme.error
    val noneMarkColor = MaterialTheme.colorScheme.tertiary

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

    val numSevenTextLayoutResult = numTextLayout("7")
    val numSevenTextSize = numSevenTextLayoutResult.size
    val numSevenColor = MaterialTheme.colorScheme.primary

    val firstCubicCenter = lineGap / 2f
    val numTextBackgroundLength = lineGap - strokeWidth
    val positionOffset = strokeWidth / 2f

    val blockColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)

    val anim = remember { Animatable(0f) }
    LaunchedEffect(anim) {
        anim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600, easing = LinearEasing)
        )
    }

    Canvas(
        modifier = Modifier.size(fullSize)
    ) {

        drawRoundRect(
            color = blockColor,
            topLeft = Offset(0f, 0f),
            size = Size(numTextBackgroundLength, numTextBackgroundLength),
            cornerRadius = CornerRadius(strokeWidth),
            alpha = anim.value * 1f
        )
        drawText(
            textLayoutResult = numTwoTextLayoutResult,
            color = numTwoColor,
            topLeft = Offset(
                firstCubicCenter - (numTwoTextSize.width / 2f) - positionOffset,
                firstCubicCenter - (numTwoTextSize.height / 2f) - positionOffset,
            ),
            alpha = anim.value * 1f,
        )

        drawRoundRect(
            color = blockColor,
            topLeft = Offset(lineGap * (1 % 3), lineGap * (1 / 3)),
            size = Size(numTextBackgroundLength, numTextBackgroundLength),
            cornerRadius = CornerRadius(strokeWidth),
            alpha = anim.value * 1f
        )
        drawText(
            textLayoutResult = numSevenTextLayoutResult,
            color = numSevenColor,
            topLeft = Offset(
                firstCubicCenter * 3 - (numSevenTextSize.width / 2f) - positionOffset,
                firstCubicCenter - (numSevenTextSize.height / 2f) - positionOffset
            ),
            alpha = anim.value * 1f,
        )

        drawRoundRect(
            color = blockColor,
            topLeft = Offset(lineGap * (4 % 3), lineGap * (4 / 3)),
            size = Size(numTextBackgroundLength, numTextBackgroundLength),
            cornerRadius = CornerRadius(strokeWidth),
            alpha = anim.value * 1f
        )
        translate(
            left = firstCubicCenter * 3 - (markSize / 2f) - positionOffset,
            top = firstCubicCenter * 3 - (markSize / 2f) - positionOffset
        ) {
            with(potentialMark) {
                draw(
                    size = Size(markSize, markSize),
                    colorFilter = ColorFilter.tint(potentialMarkColor),
                    alpha = anim.value * 1f,
                )
            }
        }

        drawRoundRect(
            color = blockColor,
            topLeft = Offset(lineGap * (7 % 3), lineGap * (7 / 3)),
            size = Size(numTextBackgroundLength, numTextBackgroundLength),
            cornerRadius = CornerRadius(strokeWidth),
            alpha = anim.value * 1f
        )
        translate(
            left = firstCubicCenter * 3 - (markSize / 2f) - positionOffset,
            top = firstCubicCenter * 5 - (markSize / 2f) - positionOffset,
        ) {
            with(wrongMark) {
                draw(
                    size = Size(markSize, markSize),
                    colorFilter = ColorFilter.tint(wrongMarkColor),
                    alpha = anim.value * 1f,
                )
            }
        }

        drawRoundRect(
            color = blockColor,
            topLeft = Offset(lineGap * (8 % 3), lineGap * (8 / 3)),
            size = Size(numTextBackgroundLength, numTextBackgroundLength),
            cornerRadius = CornerRadius(strokeWidth),
            alpha = anim.value * 1f
        )
        translate(
            left = firstCubicCenter * 5 - (markSize / 2f) - positionOffset,
            top = firstCubicCenter * 5 - (markSize / 2f) - positionOffset,
        ) {
            with(noneMark) {
                draw(
                    size = Size(markSize, markSize),
                    colorFilter = ColorFilter.tint(noneMarkColor),
                    alpha = anim.value * 1f,
                )
            }
        }

    }

}

@Preview(
    name = "Azure Block",
    showBackground = true
)
@Preview(
    name = "Azure Block",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun AzureBlockPreview() {
    AzureTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AzureBlock(192.dp)
        }
    }
}
