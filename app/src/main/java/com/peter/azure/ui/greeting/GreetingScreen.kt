/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.greeting

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.azure.R
import com.peter.azure.data.entity.Info
import com.peter.azure.ui.theme.AzureTheme
import com.peter.azure.ui.util.ErrorDialog
import com.peter.azure.ui.util.ProcessingDialog
import com.peter.azure.ui.util.azureScreen

@Composable
fun GreetingScreen(
    viewModel: GreetingViewModel,
    navigateToHome: () -> Unit,
    exitApp: () -> Unit
) {

    GreetingContent(
        loadInfo = viewModel::loadInfo,
        greetingUiState = viewModel.uiState.value,
        dismissDialog = viewModel::dismissDialog,
        agreeContracts = viewModel::agreeContracts,
        navigateToHome = navigateToHome,
        exitApp = exitApp
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun GreetingContent(
    loadInfo: (Info.Type) -> Unit,
    greetingUiState: GreetingUiState,
    dismissDialog: () -> Unit,
    agreeContracts: () -> Unit,
    navigateToHome: () -> Unit,
    exitApp: () -> Unit
) {

    when (greetingUiState) {
        is GreetingUiState.ContractsAgreed -> {
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
        is GreetingUiState.ContractDialogLoaded -> {
            ContractDialog(
                info = greetingUiState.info,
                onDismiss = dismissDialog
            )
        }
        is GreetingUiState.Processing -> {
            ProcessingDialog()
        }
        is GreetingUiState.Default -> Unit
    }

    val defaultTextSpanStyle = SpanStyle(
        color = MaterialTheme.colorScheme.onBackground
    )
    val linkTextSpanStyle = SpanStyle(
        color = MaterialTheme.colorScheme.primary.copy(0.7f),
        textDecoration = TextDecoration.Underline
    )

    Column(
        modifier = Modifier.azureScreen()
    ) {
        Text(
            text = stringResource(R.string.screen_greeting),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.weight(2f))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.screen_about_slogan),
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
        Spacer(modifier = Modifier.weight(1.5f))
        ClickableText(
            onClick = { index ->
                when (index) {
                    in 17..29 -> {
                        loadInfo(Info.Type.SERVICE)
                    }
                    in 32..45 -> {
                        loadInfo(Info.Type.PRIVACY)
                    }
                    in 51..66 -> {
                        loadInfo(Info.Type.ACKNOWLEDGEMENTS)
                    }
                }
            },
            text = buildAnnotatedString {
                withStyle(defaultTextSpanStyle) {
                    append("Please read our ")
                }
                withStyle(linkTextSpanStyle) {
                    append("service terms")
                }
                withStyle(defaultTextSpanStyle) {
                    append(", ")
                }
                withStyle(linkTextSpanStyle) {
                    append("privacy policy")
                }
                withStyle(defaultTextSpanStyle) {
                    append(" and ")
                }
                withStyle(linkTextSpanStyle) {
                    append("acknowledgements")
                }
                withStyle(defaultTextSpanStyle) {
                    append(".")
                }
            },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        DecisionButton(
            onclick = agreeContracts,
            textId = R.string.screen_greeting_accept,
            textColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .height(54.dp)
        )
        DecisionButton(
            onclick = exitApp,
            textId = R.string.screen_greeting_reject,
            textColor = MaterialTheme.colorScheme.onError,
            containerColor = MaterialTheme.colorScheme.error,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .height(54.dp)
        )
    }
}

@Preview(
    name = "Greeting Screen",
    showBackground = true
)
@Preview(
    name = "Greeting Screen", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AboutScreenPreview() {
    AzureTheme {
        GreetingContent({}, GreetingUiState.Default, {}, {}, {}, {})
    }
}
