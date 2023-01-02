package com.peter.azure.ui.about

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peter.azure.R
import com.peter.azure.data.entity.Info
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.ui.navigation.AzureTopBar
import com.peter.azure.ui.theme.AzureTheme
import com.peter.azure.ui.util.azureScreen

@Composable
fun AboutScreen(
    navigateToMainScreens: (String) -> Unit,
    navigateToContract: (String) -> Unit
) {
    val showNavDialog = remember { mutableStateOf(false) }

    AboutContent(
        navDialogState = showNavDialog,
        navigateToMainScreens = navigateToMainScreens,
        navigateToContract = navigateToContract
    )
}

@Composable
fun AboutContent(
    navDialogState: MutableState<Boolean>,
    navigateToMainScreens: (String) -> Unit,
    navigateToContract: (String) -> Unit
) {
    val gradientBrush = Brush.linearGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.tertiary
        )
    )

    Column(
        modifier = Modifier.azureScreen()
    ) {
        AzureTopBar(
            navDialogState = navDialogState,
            destination = AzureDestination.Main.ABOUT,
            navigateToMainScreens = navigateToMainScreens
        )
        Box(
            contentAlignment = Center,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(gradientBrush)
                .weight(2.5f)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.app_motto),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.padding(top = 12.dp))
        AboutAppItem(
            heading = stringResource(R.string.app_version_title),
            text = stringResource(id = R.string.app_version)
        )
        AboutAppItem(
            heading = stringResource(R.string.app_last_update_title),
            text = stringResource(id = R.string.app_last_update)
        )
        Spacer(modifier = Modifier.padding(top = 12.dp))
        AboutInfoItem(
            heading = stringResource(R.string.service_title) to Info.Type.SERVICE,
            navigateToInfo = navigateToContract
        )
        AboutInfoItem(
            heading = stringResource(R.string.privacy_title) to Info.Type.PRIVACY,
            navigateToInfo = navigateToContract
        )
        AboutInfoItem(
            heading = stringResource(R.string.acknowledgement_title) to Info.Type.ACKNOWLEDGEMENTS,
            navigateToInfo = navigateToContract
        )
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text = stringResource(id = R.string.app_website),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(
    name = "About Screen",
    showBackground = true
)
@Preview(
    name = "About Screen", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AboutScreenPreview() {
    val showNavDialog = remember { mutableStateOf(false) }
    AzureTheme {
        AboutContent(showNavDialog, {}, {})
    }
}
