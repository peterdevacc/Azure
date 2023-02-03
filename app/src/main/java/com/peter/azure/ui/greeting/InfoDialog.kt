/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.greeting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.peter.azure.R
import com.peter.azure.data.entity.Info
import com.peter.azure.ui.util.InfoDocument

@Composable
fun InfoDialog(
    info: Info,
    isPortrait: Boolean,
    onDismiss: () -> Unit
) {
    val widthFraction = if (isPortrait) 0.8f else 0.65f
    val titleDescription = stringResource(R.string.info_dialog_title_description)

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(widthFraction)
                .fillMaxHeight(0.65f)
                .semantics {
                    contentDescription = titleDescription
                    liveRegion = LiveRegionMode.Assertive
                }
        ) {
            Text(
                text = stringResource(
                    R.string.screen_greeting_info_dialog_title,
                    info.type.name
                ),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            InfoDocument(
                info = info,
                contentPadding = PaddingValues(0.dp),
                backgroundColor = MaterialTheme.colorScheme.background,
                textColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(
                        text = stringResource(R.string.screen_greeting_info_dialog_cancel),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

