/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.peter.azure.R
import com.peter.azure.data.entity.Location
import com.peter.azure.data.entity.Mark

@Composable
fun GameMarkBlock(
    location: Location,
    markList: List<Mark>,
    isCompact: Boolean
) {
    val locationTextColor: Color
    val locationBackgroundColor: Color
    val numTextColor: Color
    val frameColor: Color
    val backgroundColor: Color
    if (isSystemInDarkTheme()) {
        locationTextColor = MaterialTheme.colorScheme.primary
        locationBackgroundColor = MaterialTheme.colorScheme.background
        numTextColor = MaterialTheme.colorScheme.tertiary
        frameColor = MaterialTheme.colorScheme.tertiary
        backgroundColor = MaterialTheme.colorScheme.background
    } else {
        locationTextColor = MaterialTheme.colorScheme.onPrimaryContainer
        locationBackgroundColor = MaterialTheme.colorScheme.primaryContainer
        numTextColor = MaterialTheme.colorScheme.onTertiaryContainer
        frameColor = MaterialTheme.colorScheme.onTertiaryContainer
        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer
    }

    val locationTextStyle: TextStyle
    val numTextStyle: TextStyle
    val iconArgs: Pair<Int, Int>

    if (isCompact) {
        locationTextStyle = MaterialTheme.typography.bodyMedium
            .copy(color = locationTextColor)
        numTextStyle = MaterialTheme.typography.bodyLarge
            .copy(fontSize = 18.sp, color = numTextColor)
        iconArgs = 16 to 1
    } else {
        locationTextStyle = MaterialTheme.typography.bodyLarge
            .copy(fontSize = 20.sp, color = locationTextColor)
        numTextStyle = MaterialTheme.typography.bodyLarge
            .copy(fontSize = 24.sp, color = numTextColor)
        iconArgs = 20 to 2
    }

    val row = location.x + 1
    val column = location.y + 1
    val cellTitle = if (location.isNotDefault()) {
        stringResource(
            R.string.screen_game_mark_block_cell_title,
            row, column
        )
    } else {
        stringResource(R.string.screen_game_mark_block_cell_none_title)
    }
    val cellTitleDescription = stringResource(
        R.string.mark_block_cell_title_description,
        cellTitle
    )

    Column(
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
        Text(
            text = cellTitle,
            style = locationTextStyle,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(locationBackgroundColor)
                .padding(vertical = 2.dp)
                .fillMaxWidth()
                .semantics {
                    text = AnnotatedString(cellTitleDescription)
                }
        )
        GameMarkHorizontalDivider(frameColor)
        val times = 3
        for (i in 0..2) {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                for (j in 0..2) {
                    val num = (j + 1) + (i * times)
                    val mark = markList[num - 1]
                    val block = if (location.isNotDefault()) {
                        stringResource(
                            R.string.mark_block_num_description,
                            num, mark.name, row, column
                        )
                    } else {
                        stringResource(
                            R.string.mark_block_location_none_description
                        )
                    }
                    ConstraintLayout(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .clearAndSetSemantics {
                                text = AnnotatedString(block)
                            }
                    ) {
                        val (text, icon) = createRefs()
                        Text(
                            text = "$num",
                            style = numTextStyle,
                            modifier = Modifier.constrainAs(text) {
                                centerVerticallyTo(parent)
                                centerHorizontallyTo(parent)
                            }
                        )
                        if (mark != Mark.NONE) {
                            val markIconId: Int
                            val markIconCd: Int

                            if (mark == Mark.POTENTIAL) {
                                markIconId = R.drawable.ic_mark_potential_24
                                markIconCd = R.string.icon_cd_mark_potential
                            } else {
                                markIconId = R.drawable.ic_mark_wrong_24
                                markIconCd = R.string.icon_cd_mark_wrong
                            }

                            Icon(
                                painter = painterResource(markIconId),
                                contentDescription = stringResource(markIconCd),
                                tint = frameColor,
                                modifier = Modifier
                                    .padding(
                                        end = iconArgs.second.dp,
                                        bottom = iconArgs.second.dp
                                    )
                                    .size(iconArgs.first.dp)
                                    .constrainAs(icon) {
                                        end.linkTo(parent.end)
                                        bottom.linkTo(parent.bottom)
                                    }
                            )
                        }
                    }
                    if (j != 2) {
                        GameMarkVerticalDivider(frameColor)
                    }
                }
            }
            if (i != 2) {
                GameMarkHorizontalDivider(frameColor)
            }
        }
    }

}

@Composable
private fun GameMarkHorizontalDivider(color: Color) {
    Divider(
        thickness = 1.dp,
        color = color
    )
}

@Composable
private fun GameMarkVerticalDivider(color: Color) {
    Divider(
        color = color,
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
    )
}
