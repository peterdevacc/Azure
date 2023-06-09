/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.azure.R
import com.peter.azure.data.entity.Mark

@Composable
fun GamePad(
    blank: () -> Unit,
    mark: (Mark) -> Unit,
    write: (Int) -> Unit,
    isCompact: Boolean
) {
    val contentColor: Color
    val frameColor: Color
    val backgroundColor: Color
    if (isSystemInDarkTheme()) {
        contentColor = MaterialTheme.colorScheme.secondary
        frameColor = MaterialTheme.colorScheme.secondaryContainer
        backgroundColor = MaterialTheme.colorScheme.background
    } else {
        contentColor = Color(0xFFbd7a0c)
        frameColor = MaterialTheme.colorScheme.secondary
        backgroundColor = Color(0xFFfff6e3).copy(alpha = 0.3f)
    }

    val textSize: Int
    val iconSize: Int

    if (isCompact) {
        textSize = 18
        iconSize = 20
    } else {
        textSize = 24
        iconSize = 26
    }

    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(backgroundColor)
            .fillMaxSize()
            .border(
                width = 1.dp,
                color = frameColor,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Column(
            modifier = Modifier.weight(1.2f)
        ) {
            ActionIcon(
                iconId = R.drawable.ic_clear_24,
                iconCd = R.string.icon_cd_clean,
                iconSize = iconSize,
                tint = contentColor,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clickable(onClick = blank)
            )
            Mark.values().forEach { mark ->
                val iconId: Int
                val iconCd: Int

                when (mark) {
                    Mark.POTENTIAL -> {
                        iconId = R.drawable.ic_mark_potential_24
                        iconCd = R.string.icon_cd_mark_potential
                    }
                    Mark.WRONG -> {
                        iconId = R.drawable.ic_mark_wrong_24
                        iconCd = R.string.icon_cd_mark_wrong
                    }
                    Mark.NONE -> {
                        iconId = R.drawable.ic_mark_none_24
                        iconCd = R.string.icon_cd_mark_none
                    }
                }

                GamePadHorizontalDivider(frameColor)
                ActionIcon(
                    iconId = iconId,
                    iconCd = iconCd,
                    iconSize = iconSize,
                    tint = contentColor,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clickable { mark(mark) }
                )
            }
        }
        GamePadVerticalDivider(frameColor)
        Column(
            modifier = Modifier.weight(3.8f)
        ) {
            val times = 3
            for (i in 0..2) {
                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    for (j in 0..2) {
                        val num = (j + 1) + (i * times)
                        val numDescription = stringResource(
                            R.string.game_pad_num_button_description, num
                        )
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { write(num) }
                            ) {
                                Text(
                                    text = "$num",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = contentColor,
                                    fontSize = textSize.sp,
                                    modifier = Modifier.semantics {
                                        text = AnnotatedString(numDescription)
                                    }
                                )
                            }
                        }
                        if (j != 2) {
                            GamePadVerticalDivider(frameColor)
                        }
                    }
                }
                if (i != 2) {
                    GamePadHorizontalDivider(frameColor)
                }
            }
        }
    }
}

@Composable
private fun ActionIcon(
    iconId: Int,
    iconCd: Int,
    iconSize: Int,
    tint: Color,
    modifier: Modifier,
) {
    val buttonDescription = stringResource(
        R.string.game_pad_icon_button_description,
        stringResource(iconCd)
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = "",
            tint = tint,
            modifier = Modifier
                .size(iconSize.dp)
                .clearAndSetSemantics {
                    text = AnnotatedString(buttonDescription)
                }
        )
    }
}

@Composable
private fun GamePadHorizontalDivider(color: Color) {
    Divider(
        thickness = 1.dp,
        color = color
    )
}

@Composable
private fun GamePadVerticalDivider(color: Color) {
    Divider(
        color = color,
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
    )
}
