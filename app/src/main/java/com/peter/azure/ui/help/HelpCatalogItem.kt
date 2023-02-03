/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.help

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun HelpCatalogItem(
    catalogName: String,
    catalogIconId: Int,
    color: Color,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clearAndSetSemantics {
            text = AnnotatedString(catalogName)
        }
    ) {
        Icon(
            painter = painterResource(catalogIconId),
            contentDescription = "",
            modifier = Modifier.size(26.dp),
            tint = color,
        )
        Spacer(modifier = Modifier.padding(horizontal = 6.dp))
        Text(
            text = catalogName,
            style = MaterialTheme.typography.titleLarge,
            color = color,
            modifier = Modifier.weight(1f)
        )
    }
}
