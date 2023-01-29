/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.game

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    val locationTextStyle: TextStyle
    val markTextStyle: TextStyle
    val iconArgs: Pair<Int, Int>

    if (isCompact) {
        locationTextStyle = MaterialTheme.typography.bodyMedium
            .copy(color = MaterialTheme.colorScheme.onPrimaryContainer)
        markTextStyle = MaterialTheme.typography.bodyLarge
            .copy(
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        iconArgs = 16 to 1
    } else {
        locationTextStyle = MaterialTheme.typography.bodyLarge
            .copy(
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        markTextStyle = MaterialTheme.typography.bodyLarge
            .copy(
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
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
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .fillMaxSize()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Text(
            text = cellTitle,
            style = locationTextStyle,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 2.dp)
                .fillMaxWidth()
                .semantics {
                    text = AnnotatedString(cellTitleDescription)
                }
        )
        GameMarkHorizontalDivider()
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
                            style = markTextStyle,
                            modifier = Modifier.constrainAs(text) {
                                centerVerticallyTo(parent)
                                centerHorizontallyTo(parent)
                            }
                        )
                        when (mark) {
                            Mark.POTENTIAL -> {
                                GameMarkIcon(
                                    R.drawable.ic_mark_potential_24,
                                    R.string.icon_cd_mark_potential,
                                    Modifier
                                        .padding(
                                            end = iconArgs.second.dp, bottom = iconArgs.second.dp
                                        )
                                        .size(iconArgs.first.dp)
                                        .constrainAs(icon) {
                                            end.linkTo(parent.end)
                                            bottom.linkTo(parent.bottom)
                                        }
                                )
                            }
                            Mark.WRONG -> {
                                GameMarkIcon(
                                    R.drawable.ic_mark_wrong_24,
                                    R.string.icon_cd_mark_wrong,
                                    Modifier
                                        .padding(
                                            end = iconArgs.second.dp, bottom = iconArgs.second.dp
                                        )
                                        .size(iconArgs.first.dp)
                                        .constrainAs(icon) {
                                            end.linkTo(parent.end)
                                            bottom.linkTo(parent.bottom)
                                        }
                                )
                            }
                            Mark.NONE -> {
                                Spacer(
                                    modifier = Modifier
                                        .padding(bottom = 1.dp)
                                        .size(16.dp)
                                        .constrainAs(icon) {
                                            end.linkTo(parent.end)
                                            bottom.linkTo(parent.bottom)
                                        }
                                )
                            }
                        }
                    }
                    if (j != 2) {
                        GameMarkVerticalDivider()
                    }
                }
            }
            if (i != 2) {
                GameMarkHorizontalDivider()
            }
        }
    }

}

@Composable
private fun GameMarkIcon(
    @DrawableRes
    markIconId: Int,
    markIconContentDescriptionId: Int,
    modifier: Modifier
) {
    Icon(
        painter = painterResource(markIconId),
        contentDescription = stringResource(markIconContentDescriptionId),
        tint = MaterialTheme.colorScheme.onTertiaryContainer,
        modifier = modifier
    )
}

@Composable
private fun GameMarkHorizontalDivider() {
    Divider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outline
    )
}

@Composable
private fun GameMarkVerticalDivider() {
    Divider(
        color = MaterialTheme.colorScheme.outline,
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
    )
}
