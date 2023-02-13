/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.azure.ui.theme.AzureTheme

@Composable
fun AboutAppItem(
    heading: String, text: String,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .clearAndSetSemantics {
                this.text = AnnotatedString(
                    text = "$heading, $text"
                )
            }
    ) {
        Text(
            text = heading,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline,
            fontSize = 20.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutAppItemPreview() {
    AzureTheme {
        AboutAppItem(
            "app name",
            "AzureSudoku",
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}
