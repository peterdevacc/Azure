/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peter.azure.R
import com.peter.azure.data.util.DataResult
import com.peter.azure.ui.theme.AzureTheme

@Composable
fun ErrorNotice(code: DataResult.Error.Code) {
    val reason = when (code) {
        DataResult.Error.Code.UNKNOWN -> stringResource(R.string.error_unknown)
        DataResult.Error.Code.IO -> stringResource(R.string.error_io)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.error)
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.error),
            contentDescription = stringResource(R.string.icon_error),
            tint = MaterialTheme.colorScheme.onError,
            modifier = Modifier
                .size(128.dp)
        )
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Text(
            text = reason,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onError,
            maxLines = 3
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorNoticePreview() {
    AzureTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ErrorNotice(
                DataResult.Error.Code.UNKNOWN
            )
        }
    }
}
