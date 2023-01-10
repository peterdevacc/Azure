/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.greeting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.peter.azure.data.entity.Info
import com.peter.azure.ui.util.InfoDocument

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ContractDialog(
    info: Info,
    isPortrait: Boolean,
    onDismiss: () -> Unit
) {
    val widthFraction = if (isPortrait) 0.8f else 0.65f

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(4.dp)
                .fillMaxWidth(widthFraction)
                .fillMaxHeight(0.65f)
        ) {
            InfoDocument(
                info = info,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

