/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.help

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.ui.navigation.AzureTopBar
import com.peter.azure.ui.util.ErrorNotice
import com.peter.azure.ui.util.azureScreen

@Composable
fun HelpScreen(
    viewModel: HelpViewModel,
    isPortrait: Boolean,
    navigateToMainScreens: (String) -> Unit
) {
    val navDialogState = remember { mutableStateOf(false) }

    HelpContent(
        uiState = viewModel.uiState.value,
        isPortrait = isPortrait,
        navDialogState = navDialogState,
        navigateToMainScreens = navigateToMainScreens
    )
}

@Composable
private fun HelpContent(
    uiState: HelpUiState,
    isPortrait: Boolean,
    navDialogState: MutableState<Boolean>,
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

        Box(topBarModifier) {
            AzureTopBar(
                isPortrait = isPortrait,
                navDialogState = navDialogState,
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
                    HelpList(isPortrait, uiState.helpMap)
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

//@Preview(name = "Help Screen", showBackground = true)
//@Preview(
//    name = "Help Screen (dark)", showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun HelpScreenPreview() {
//    val viewModel = HelpViewModel()
//    val navHostController = rememberNavController()
//    AzureTheme {
//        HelpScreen(viewModel, navHostController)
//    }
//}
