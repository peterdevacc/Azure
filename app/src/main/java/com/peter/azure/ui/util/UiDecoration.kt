/*
 * Copyright (c) 2023 洪振健 All rights reserved.
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
