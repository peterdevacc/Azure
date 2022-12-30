package com.peter.azure.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.peter.azure.R
import com.peter.azure.data.entity.Info
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.ui.navigation.AzureTopBar
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
    Column(
        modifier = Modifier.azureScreen()
    ) {
        AzureTopBar(
            navDialogState = navDialogState,
            destination = AzureDestination.Main.ABOUT,
            navigateToMainScreens = navigateToMainScreens
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.example_img),
            contentDescription = "dummy logo",
            modifier = Modifier
                .padding(top = 32.dp, bottom = 16.dp)
                .size(128.dp)
                .align(CenterHorizontally),
            contentScale = ContentScale.FillBounds
        )
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
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(id = R.string.app_website),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

//@Preview(
//    name = "About Screen",
//    showBackground = true
//)
//@Preview(
//    name = "About Screen", showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun AboutScreenPreview() {
//    AzureTheme {
//        AboutContent(false, {}, {}, {}, {})
//    }
//}
