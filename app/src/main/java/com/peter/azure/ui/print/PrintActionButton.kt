/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.print

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun PrintActionButton(
    textId: Int,
    onClick: () -> Unit,
    uiState: PdfUiState,
    modifier: Modifier
) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        enabled = uiState != PdfUiState.Processing,
        modifier = modifier
    ) {
        Text(
            text = stringResource(textId),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
