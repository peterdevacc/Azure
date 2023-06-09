/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.greeting

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peter.azure.R
import com.peter.azure.data.entity.Info
import com.peter.azure.ui.util.ErrorDialog
import com.peter.azure.ui.util.ProcessingDialog
import com.peter.azure.ui.util.azureScreen

@Composable
fun GreetingScreen(
    viewModel: GreetingViewModel,
    navigateToHome: () -> Unit,
    exitApp: () -> Unit,
    isPortrait: Boolean,
    isCompact: Boolean,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    GreetingContent(
        loadInfo = viewModel::loadInfo,
        greetingUiState = uiState,
        dismissDialog = viewModel::dismissDialog,
        agreeContracts = viewModel::acceptContracts,
        isPortrait = isPortrait,
        isCompact = isCompact,
        navigateToHome = navigateToHome,
        exitApp = exitApp,
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun GreetingContent(
    loadInfo: (Info.Type) -> Unit,
    greetingUiState: GreetingUiState,
    dismissDialog: () -> Unit,
    agreeContracts: () -> Unit,
    isPortrait: Boolean,
    isCompact: Boolean,
    navigateToHome: () -> Unit,
    exitApp: () -> Unit,
) {
    when (greetingUiState) {
        is GreetingUiState.InfoAccepted -> {
            LaunchedEffect(Unit) {
                navigateToHome()
            }
        }
        is GreetingUiState.Error -> {
            ErrorDialog(
                code = greetingUiState.code,
                onDismiss = dismissDialog
            )
        }
        is GreetingUiState.InfoDialogLoaded -> {
            InfoDialog(
                info = greetingUiState.info,
                isPortrait = isPortrait,
                onDismiss = dismissDialog
            )
        }
        is GreetingUiState.Processing -> {
            ProcessingDialog()
        }
        is GreetingUiState.Default -> Unit
    }

    val destinationText = stringResource(R.string.screen_greeting)
    val screenHeadingDescription = stringResource(
        R.string.screen_heading_description, destinationText
    )

    Column(
        modifier = Modifier.azureScreen()
    ) {
        Text(
            text = destinationText,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.semantics {
                text = AnnotatedString(screenHeadingDescription)
                liveRegion = LiveRegionMode.Assertive
            }
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    brush = Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.colorScheme.tertiary,
                        )
                    )
                ),
                textAlign = TextAlign.Center,
            )
        }

        LegalText(
            isCompact =  isCompact,
            loadInfo =  loadInfo
        )

        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (acceptButton, rejectButton, centerSpacer) = createRefs()

            val acceptButtonModifier: Modifier
            val rejectButtonModifier: Modifier
            val centerSpacerModifier: Modifier

            if (isPortrait && isCompact) {
                acceptButtonModifier = Modifier.constrainAs(acceptButton) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(centerSpacer.top)
                    width = Dimension.fillToConstraints
                }
                rejectButtonModifier = Modifier.constrainAs(rejectButton) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(centerSpacer.bottom)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
                centerSpacerModifier = Modifier
                    .padding(vertical = 8.dp)
                    .constrainAs(centerSpacer) {
                        centerVerticallyTo(parent)
                    }
            } else {
                acceptButtonModifier = Modifier
                    .padding(bottom = 16.dp)
                    .constrainAs(acceptButton) {
                        start.linkTo(centerSpacer.end)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }
                rejectButtonModifier = Modifier
                    .padding(bottom = 16.dp)
                    .constrainAs(rejectButton) {
                        start.linkTo(parent.start)
                        end.linkTo(centerSpacer.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }
                centerSpacerModifier = Modifier
                    .padding(horizontal = 8.dp)
                    .constrainAs(centerSpacer) {
                        centerHorizontallyTo(parent)
                    }
            }

            Button(
                onClick = agreeContracts,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = acceptButtonModifier.height(54.dp),
            ) {
                Text(
                    text = stringResource(R.string.screen_greeting_accept),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = centerSpacerModifier)
            Button(
                onClick = exitApp,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = rejectButtonModifier.height(54.dp),
            ) {
                Text(
                    text = stringResource(R.string.screen_greeting_reject),
                    color = MaterialTheme.colorScheme.onError,
                    fontSize = 20.sp
                )
            }
        }
    }
}
