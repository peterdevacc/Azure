/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.peter.azure.data.entity.Info

@Composable
fun InfoDocument(
    info: Info,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    modifier: Modifier
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier
            .then(Modifier)
            .background(backgroundColor)
    ) {
        itemsIndexed(info.data) { key, data ->
            when (data.type) {
                Info.Item.Type.TITLE -> {
                    val paddingValues = if (key == 0) {
                        PaddingValues(bottom = 4.dp)
                    } else {
                        PaddingValues(top = 8.dp, bottom = 4.dp)
                    }
                    Text(
                        text = data.text,
                        style = MaterialTheme.typography.titleSmall,
                        color = textColor,
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxWidth()
                    )
                }
                Info.Item.Type.TEXT -> {
                    Text(
                        text = data.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth()
                    )
                }
                Info.Item.Type.SIGNATURE -> {
                    Text(
                        text = data.text,
                        style = MaterialTheme.typography.labelMedium,
                        color = textColor,
                        modifier = Modifier
                            .padding(top = 32.dp, bottom = 8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
