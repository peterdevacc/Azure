/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.peter.azure.ui.navigation.AzureNavigationGraph
import com.peter.azure.ui.util.ErrorNotice

@Composable
fun MainContent(
    uiState: MainUiState,
    isPortrait: Boolean,
    isCompact: Boolean,
    navHostController: NavHostController,
    finish: () -> Unit
) {
    when (uiState) {
        is MainUiState.Success -> {
            AzureNavigationGraph(
                isPortrait = isPortrait,
                isCompact = isCompact,
                navHostController = navHostController,
                startDestination = uiState.startDestination,
                exitApp = finish
            )
        }
        is MainUiState.Error -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                ErrorNotice(uiState.code)
            }
        }
        MainUiState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp, 24.dp)
                )
            }
        }
    }
}