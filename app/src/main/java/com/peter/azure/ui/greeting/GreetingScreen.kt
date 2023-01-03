package com.peter.azure.ui.greeting

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
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
            navigateToHome()
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

    val underlineStyle = SpanStyle(
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
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(
                        Brush.sweepGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.tertiary,
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.azure_logo_x),
                    contentDescription = stringResource(R.string.icon_cd_azure_logo),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .size(128.dp)
                        .align(Alignment.CenterHorizontally),
                )
                Text(
                    text = stringResource(R.string.screen_about_slogan),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
//                modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.weight(1.5f))
        ClickableText(
            onClick = { index ->
                when (index) {
                    in 17..32 -> {
                        loadInfo(Info.Type.SERVICE)
                    }
                    in 35..48 -> {
                        loadInfo(Info.Type.PRIVACY)
                    }
                    in 54..69 -> {
                        loadInfo(Info.Type.ACKNOWLEDGEMENTS)
                    }
                }
            },
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    append("Please read our ")
                }
                withStyle(
                    style = underlineStyle
                ) {
                    append("terms of service")
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    append(", ")
                }
                withStyle(
                    style = underlineStyle
                ) {
                    append("privacy policy")
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    append(" and ")
                }
                withStyle(
                    style = underlineStyle
                ) {
                    append("acknowledgements")
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    append(".")
                }
            },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp)
        )
        ContractDecisionButton(
            text = "Accept and continue",
            decision = agreeContracts
        )
        ContractDecisionButton(
            text = "Reject and exit",
            decision = exitApp
        )
    }
}

@Composable
private fun ContractDecisionButton(
    text: String,
    decision: () -> Unit
) {
    Button(
        onClick = decision,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(54.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text.uppercase(),
            fontSize = 20.sp
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
