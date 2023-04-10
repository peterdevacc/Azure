/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.help

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.ui.util.AzureTopBar
import com.peter.azure.ui.util.ErrorNotice
import com.peter.azure.ui.util.azureScreen

@Composable
fun HelpScreen(
    viewModel: HelpViewModel,
    isPortrait: Boolean,
    navigateToMainScreens: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.initHelpData(Locale.current.language)
    }

    HelpContent(
        uiState = uiState,
        isPortrait = isPortrait,
        navigateToMainScreens = navigateToMainScreens
    )
}

@Composable
private fun HelpContent(
    uiState: HelpUiState,
    isPortrait: Boolean,
    navigateToMainScreens: (String) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier.azureScreen()
    ) {
        val (topBar, helpContainer) = createRefs()

        val topBarModifier: Modifier
        val helpContainerModifier: Modifier

        if (isPortrait) {
            topBarModifier = Modifier.constrainAs(topBar) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
            helpContainerModifier = Modifier.constrainAs(helpContainer) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(topBar.bottom)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        } else {
            topBarModifier = Modifier.constrainAs(topBar) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }
            helpContainerModifier = Modifier.constrainAs(helpContainer) {
                start.linkTo(topBar.end)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        }

        Box(modifier = topBarModifier) {
            AzureTopBar(
                isPortrait = isPortrait,
                destination = AzureDestination.Main.HELP,
                navigateToMainScreens = navigateToMainScreens
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = helpContainerModifier
        ) {
            when (uiState) {
                is HelpUiState.Error -> {
                    ErrorNotice(uiState.code)
                }
                is HelpUiState.Success -> {
                    HelpSectionList(isPortrait, uiState.helpMap)
                }
                is HelpUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
            }
        }
    }
}
