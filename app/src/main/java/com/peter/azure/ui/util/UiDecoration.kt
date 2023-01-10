/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.util

import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp

fun Modifier.azureScreen(): Modifier = this.composed {
    Modifier
        .windowInsetsPadding(insets = WindowInsets.systemBars)
        .padding(16.dp)
        .fillMaxSize()
}
