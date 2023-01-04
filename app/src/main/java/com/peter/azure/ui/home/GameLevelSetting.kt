package com.peter.azure.ui.home

import android.graphics.Paint
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.azure.R
import com.peter.azure.data.entity.GameLevel
import com.peter.azure.ui.theme.AzureTheme
import kotlin.math.*

@Composable
fun GameLevelSetting(
    fullSize: Dp,
    dialAngle: Double,
    setGameLevel: (Double) -> Unit,
) {

    val dial = @Composable {
        Dial(
            fullSize = fullSize,
            dialAngle = dialAngle,
            setGameLevel = setGameLevel
        )
    }
    val indicator = @Composable {
        Indicator(height = fullSize / 2.6f)
    }

    Layout(
        contents = listOf(dial, indicator),
    ) { (dialMeasure, indicatorMeasure), constraints ->

        val dialPlaceable = dialMeasure.first().measure(constraints)
        val totalHeight = dialPlaceable.height
        val totalWidth = dialPlaceable.width

        val indicatorPlaceable = indicatorMeasure.first().measure(constraints)
        val indicatorX = (totalWidth - indicatorPlaceable.width) / 2f
        val indicatorY = totalHeight / 2 - indicatorPlaceable.height * 0.8f

        layout(totalWidth, totalHeight) {

            dialPlaceable.place(0, 0, 1f)
            indicatorPlaceable.place(
                indicatorX.toInt(), indicatorY.toInt(), 2f
            )

        }

    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Dial(
    fullSize: Dp,
    dialAngle: Double,
    setGameLevel: (Double) -> Unit,
) {
    var sizeParam = fullSize
    if (sizeParam < 192.dp) {
        sizeParam = 192.dp
    }

    val density = LocalDensity.current
    val fullSizePx = with(density) { sizeParam.toPx() }

    val centerX = fullSizePx / 2f
    val centerY = fullSizePx / 2f
    val centerOffset = Offset(centerX, centerY)
    val dialStrokeWidth = fullSizePx / 64f
    val radius = fullSizePx / 2f - dialStrokeWidth
    val divideStrokeWidth = fullSizePx / 96f
    val dividerStartAngleBase = 90f
    val gap = fullSizePx / 24f
    val sweepAngle = 360f / GameLevel.values().size
    val startAngleBase = sweepAngle - dividerStartAngleBase

    val gameLevelTextList = stringArrayResource(R.array.game_level)
    val gameLevelTextSize = fullSizePx / 16f
    val gameLevelTextColor = MaterialTheme.colorScheme.primary

    var viewRotation = remember { 0.0 }
    var spinRotation = remember { 0.0 }

    val primaryColor = MaterialTheme.colorScheme.primary
    val primaryContainerColor = MaterialTheme.colorScheme.primaryContainer
    val gradientBrush = Brush.linearGradient(
        listOf(primaryColor, primaryContainerColor)
    )

    Canvas(
        modifier = Modifier
            .size(sizeParam)
            .pointerInteropFilter { event ->
                val x = event.x
                val y = event.y

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        viewRotation = dialAngle
                        spinRotation = getRotationDegree(
                            (x - centerX).toDouble(),
                            (centerY - y).toDouble()
                        )
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val newSpinRotation = getRotationDegree(
                            (x - centerX).toDouble(),
                            (centerY - y).toDouble()
                        )
                        setGameLevel(
                            viewRotation + newSpinRotation - spinRotation
                        )
                    }
                    MotionEvent.ACTION_UP -> {
                        spinRotation = 0.0
                    }
                }

                true
            }
            .graphicsLayer {
                rotationZ = dialAngle.toFloat()
            }
    ) {

        drawCircle(
            brush = gradientBrush, radius = radius, center = centerOffset,
            style = Stroke(dialStrokeWidth)
        )

        val arcPositionA = getDotPositionOnCircle(
            angle = 135f, offset = centerOffset, radius = radius
        )
        val arcPositionB = getDotPositionOnCircle(
            angle = 315f, offset = centerOffset, radius = radius
        )

        drawIntoCanvas {

            GameLevel.values().forEachIndexed { index, gameLevel ->

                val dot = getDotPositionOnCircle(
                    angle = dividerStartAngleBase + index * sweepAngle,
                    offset = centerOffset,
                    radius = radius
                )
                drawLine(
                    brush = gradientBrush,
                    strokeWidth = divideStrokeWidth,
                    start = centerOffset,
                    end = Offset(x = dot.first, y = dot.second)
                )

                val startAngle = startAngleBase + index * sweepAngle
                val path = android.graphics.Path().apply {
                    addArc(
                        arcPositionA.first - gap,
                        arcPositionA.second - gap,
                        arcPositionB.first + gap,
                        arcPositionB.second + gap,
                        startAngle,
                        sweepAngle
                    )
                }
                it.nativeCanvas.drawTextOnPath(
                    gameLevelTextList[gameLevel.num],
                    path,
                    0f,
                    0f,
                    Paint().apply {
                        textSize = gameLevelTextSize
                        textAlign = Paint.Align.CENTER
                        color = gameLevelTextColor.toArgb()
                    }
                )

            }

        }

    }

}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun Indicator(
    height: Dp
) {
    val minHeight = 64.dp
    var heightParam = height
    if (heightParam < minHeight) {
        heightParam = minHeight
    }
    val widthParam = heightParam / 4f

    val density = LocalDensity.current
    val widthPx = with(density) { widthParam.toPx() }
    val heightPx = with(density) { heightParam.toPx() }
    val centerX = widthPx / 2f

    val bottomHeight = heightPx * 0.9f
    val topHeight = heightPx * 0.08f

    val fontSize = (widthParam.value * 0.875f).toInt()
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult = textMeasurer.measure(
        text = AnnotatedString("A"),
        style = TextStyle(fontSize = fontSize.sp)
    )
    val textSize = textLayoutResult.size

    val indicatorColor = MaterialTheme.colorScheme.inversePrimary
    val coreColor = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = Modifier
            .width(widthParam)
            .height(heightParam)
    ) {

        val indicatorPath = Path().apply {
            moveTo(x = centerX, y = heightPx)
            lineTo(x = 0f, y = bottomHeight)
            lineTo(x = widthPx * 0.2f, y = topHeight)
            lineTo(x = centerX, y = 0f)
            lineTo(x = widthPx * 0.8f, y = topHeight)
            lineTo(x = widthPx, y = bottomHeight)
            lineTo(x = centerX, y = heightPx)
            close()
        }

        drawPath(
            path = indicatorPath,
            color = indicatorColor,
        )

        drawCircle(
            color = coreColor,
            radius = widthPx / 3.2f,
            center = Offset(x = centerX, y = bottomHeight * 0.94f),
        )

        drawText(
            textMeasurer = textMeasurer,
            text = "A",
            style = TextStyle(
                fontSize = fontSize.sp,
                color = coreColor
            ),
            topLeft = Offset(
                x = (widthPx - textSize.width) / 2f + 0.1f,
                y = bottomHeight * 0.88f - textSize.height
            )
        )

        drawLine(
            color = coreColor,
            start = Offset(x = centerX, y = bottomHeight * 0.58f),
            end = Offset(x = centerX, y = topHeight),
            strokeWidth = fontSize / 2.2f
        )

    }

}

private fun getDotPositionOnCircle(
    angle: Float, offset: Offset, radius: Float
): Pair<Float, Float> {
    return Pair(
        offset.x + radius * cos((-angle * PI / 180f).toFloat()),
        offset.y + radius * sin((-angle * PI / 180f).toFloat())
    )
}

private fun getRotationDegree(
    x: Double, y: Double
): Double {
    return Math.toDegrees(atan2(x, y))
}

@Preview(name = "GameLevelSetting", showBackground = true)
//@Preview(
//    name = "GameLevelSetting (dark)", showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
@Composable
fun GameLevelSettingPreview() {
    val angle = remember {
        mutableStateOf(0.0)
    }
    AzureTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            GameLevelSetting(
                256.dp, angle.value
            ) { angle.value = it }
        }
    }
}
