/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.greeting

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit

@Composable
fun DecisionButton(
    textId: Int,
    fontSize: TextUnit,
    textColor: Color,
    containerColor: Color,
    onclick: () -> Unit,
    modifier: Modifier,
) {
    Button(
        onClick = onclick,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        ),
        modifier = modifier,
    ) {
        Text(
            text = stringResource(textId),
            color = textColor,
            fontSize = fontSize
        )
    }
}
