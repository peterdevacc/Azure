package com.peter.azure.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.peter.azure.ui.navigation.AzureNavigationGraph
import com.peter.azure.ui.util.ErrorNotice

@Composable
fun MainContent(
    uiState: MainUiState,
    navHostController: NavHostController,
    finish: () -> Unit
) {
    when (uiState) {
        is MainUiState.Success -> {
            AnimatedVisibility(
                visible = uiState != MainUiState.Loading,
                enter = fadeIn(
                    animationSpec = tween(600)
                ),
                exit = fadeOut(
                    animationSpec = tween(600)
                )
            ) {
                AzureNavigationGraph(
                    navHostController = navHostController,
                    startDestination = uiState.startDestination,
                    exitApp = { finish() }
                )
            }
        }
        is MainUiState.Error -> {
            AnimatedVisibility(
                visible = uiState != MainUiState.Loading,
                enter = fadeIn(
                    animationSpec = tween(600)
                ),
                exit = fadeOut(
                    animationSpec = tween(600)
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    ErrorNotice(uiState.code)
                }
            }
        }
        MainUiState.Loading -> Unit
    }
}