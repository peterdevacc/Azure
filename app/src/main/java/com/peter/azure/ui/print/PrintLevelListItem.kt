/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.print

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.peter.azure.R

@Composable
fun PrintLevelListItem(
    text: String,
    onClick: () -> Unit,
    index: Int,
    modifier: Modifier
) {
    val position = index + 1
    OutlinedButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.secondary
        ),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondaryContainer),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.semantics {
                this.text = AnnotatedString("")
            }
        )
        Icon(
            painter = painterResource(R.drawable.ic_clear_24),
            contentDescription = stringResource(
                R.string.icon_cd_remove_level,
                position,text
            ),
            modifier = Modifier.size(16.dp)
        )
    }
}
