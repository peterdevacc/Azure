/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.info

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
import com.peter.azure.ui.util.AzureTopBar
import com.peter.azure.ui.util.ErrorNotice
import com.peter.azure.ui.util.InfoDocument
import com.peter.azure.ui.util.azureScreen

@Composable
fun InfoScreen(
    viewModel: InfoViewModel,
    isPortrait: Boolean,
    navigateUp: () -> Unit
) {
    InfoContent(
        uiState = viewModel.uiState.value,
        isPortrait = isPortrait,
        navigateUp = navigateUp
    )
}

@Composable
private fun InfoContent(
    uiState: InfoUiState,
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

        Box(modifier = topBarModifier) {
            AzureTopBar(
                isPortrait = isPortrait,
                destination = AzureDestination.General.INFO,
                navigateUp = navigateUp
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = infoDocumentModifier
        ) {
            when (uiState) {
                is InfoUiState.Error -> {
                    ErrorNotice(uiState.code)
                }
                is InfoUiState.Success -> {
                    InfoDocument(
                        info = uiState.info,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.large)
                    )
                }
                is InfoUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
            }
        }
    }
}
