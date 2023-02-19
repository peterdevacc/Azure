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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.azure.R
import com.peter.azure.data.entity.Cell
import com.peter.azure.data.entity.Location
import com.peter.azure.data.entity.Puzzle

@Composable
fun GameBoard(
    puzzle: Puzzle,
    selectedLocation: Location,
    select: (Location, Int) -> Unit,
    isCompact: Boolean
) {
    val frameColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    } else {
        MaterialTheme.colorScheme.onBackground
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .border(1.dp, frameColor)
    ) {
        for (i in 0..8) {
            Row(modifier = Modifier.weight(1f)) {
                for (j in 0..8) {
                    val cell = puzzle.getCell(i, j)
                    GameCell(
                        cell = cell,
                        location = Location(i, j),
                        selectedLocation = selectedLocation,
                        select = select,
                        isCompact = isCompact,
                        modifier = Modifier.weight(1f)
                    )
                    if (j != 8) {
                        Divider(
                            color = frameColor,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                        )
                    }
                }
            }
            if (i != 8) {
                Divider(
                    thickness = 1.dp,
                    color = frameColor
                )
            }
        }
    }
}

@Composable
private fun GameCell(
    cell: Cell,
    location: Location,
    selectedLocation: Location,
    select: (Location, Int) -> Unit,
    isCompact: Boolean,
    modifier: Modifier
) {
    val row = location.x + 1
    val column = location.y + 1
    val boxModifier: Modifier
    val cellDescription: String
    val textColor: Color

    if (cell.type == Cell.Type.BLANK) {
        boxModifier = Modifier
            .then(modifier)
            .background(
                color = if (location == selectedLocation) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.background
                }
            )
            .clickable { select(location, cell.num) }

        cellDescription = if (cell.num != 0) {
            stringResource(
                R.string.game_board_blank_cell_with_num_description,
                row, column, cell.num
            )
        } else {
            stringResource(
                R.string.game_board_blank_cell_description,
                row, column
            )
        }

        textColor = if (location == selectedLocation) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    } else {
        boxModifier = Modifier
            .then(modifier)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(0.7f))

        cellDescription = stringResource(
            R.string.game_board_question_cell_description,
            row, column, cell.num
        )

        textColor = MaterialTheme.colorScheme.onBackground
    }

    val numString = if (cell.num != 0) {
        "${cell.num}"
    } else {
        ""
    }

    val textStyle = if (isCompact) {
        MaterialTheme.typography.bodyLarge.copy(
            color = textColor
        )
    } else {
        MaterialTheme.typography.bodyLarge.copy(
            fontSize = 20.sp,
            color = textColor
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = boxModifier
            .fillMaxSize()
            .clearAndSetSemantics {
                text = AnnotatedString(cellDescription)
            }
    ) {
        Text(
            text = numString,
            style = textStyle
        )
    }

}
