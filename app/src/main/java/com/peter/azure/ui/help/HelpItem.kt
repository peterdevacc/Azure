/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.help

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.peter.azure.data.entity.Help

@Composable
fun HelpItem(
    help: Help,
    colorPair: Pair<Color, Color>,
    modifier: Modifier
) {
    Column(
        modifier = modifier.clearAndSetSemantics {
            text = AnnotatedString(
                "${help.title}, ${help.text}"
            )
        }
    ) {
        Text(
            text = help.title,
            style = MaterialTheme.typography.titleMedium,
            color = colorPair.second,
            modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
        )
        Text(
            text = help.text,
            style = MaterialTheme.typography.bodyLarge,
            color = colorPair.second.copy(0.9f),
            modifier = Modifier
                .padding(vertical = 2.dp)
                .fillMaxWidth()
        )
    }
}
