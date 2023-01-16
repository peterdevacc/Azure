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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.peter.azure.R
import com.peter.azure.data.entity.Mark

@Composable
fun GameMarkBlock(
    markList: List<Mark>,
    isCompact: Boolean
) {
    val textStyle: TextStyle
    val iconArgs: Pair<Int, Int>

    if (isCompact) {
        textStyle = MaterialTheme.typography.bodyLarge
            .copy(color = MaterialTheme.colorScheme.onTertiaryContainer)
        iconArgs = 16 to 1
    } else {
        textStyle = MaterialTheme.typography.bodyLarge
            .copy(
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        iconArgs = 20 to 2
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f))
            .border(1.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        val times = 3
        for (i in 0..2) {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                for (j in 0..2) {
                    val num = (j + 1) + (i * times)
                    val mark = markList[num - 1]
                    ConstraintLayout(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    ) {
                        val (text, icon) = createRefs()
                        Text(
                            text = "$num",
                            style = textStyle,
                            modifier = Modifier.constrainAs(text) {
                                centerVerticallyTo(parent)
                                centerHorizontallyTo(parent)
                            }
                        )
                        when (mark) {
                            Mark.Potential -> {
                                MarkIcon(
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
                                MarkIcon(
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
                        Divider(
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                        )
                    }
                }
            }
            if (i != 2) {
                Divider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }

}

@Composable
private fun MarkIcon(
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

