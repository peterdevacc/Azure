/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.contract

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.ui.navigation.AzureTopBar
import com.peter.azure.ui.util.ErrorNotice
import com.peter.azure.ui.util.InfoDocument
import com.peter.azure.ui.util.azureScreen

@Composable
fun ContractScreen(
    viewModel: ContractViewModel,
    isPortrait: Boolean,
    navigateUp: () -> Unit
) {
    ContractContent(
        uiState = viewModel.uiState.value,
        isPortrait = isPortrait,
        navigateUp = navigateUp
    )
}

@Composable
fun ContractContent(
    uiState: ContractUiState,
    isPortrait: Boolean,
    navigateUp: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier.azureScreen()
    ) {
        val (topBar, infoDocument) = createRefs()

        val topBarModifier: Modifier
        val infoDocumentModifier: Modifier

        if (isPortrait) {
            topBarModifier = Modifier.constrainAs(topBar) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
            infoDocumentModifier = Modifier.constrainAs(infoDocument) {
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
            infoDocumentModifier = Modifier
                .padding(start = 16.dp)
                .constrainAs(infoDocument) {
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
                destination = AzureDestination.General.CONTRACT,
                navigateUp = navigateUp
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = infoDocumentModifier
        ) {
            when (uiState) {
                is ContractUiState.Error -> {
                    ErrorNotice(uiState.code)
                }
                is ContractUiState.Success -> {
                    InfoDocument(
                        info = uiState.info,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.large)
                    )
                }
                is ContractUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
            }
        }
    }
}

//@Preview(name = "Contract Screen", showBackground = true)
//@Preview(
//    name = "Contract Screen", showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun ContractScreenPreview() {
//    AzureTheme {
//
//    }
//}
