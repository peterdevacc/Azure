/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.about

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.peter.azure.R
import com.peter.azure.data.entity.Info
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.ui.navigation.AzureTopBar
import com.peter.azure.ui.theme.AzureTheme
import com.peter.azure.ui.util.azureScreen

@Composable
fun AboutScreen(
    navigateToContract: (Info.Type) -> Unit,
    isPortrait: Boolean,
    isCompact: Boolean,
    navigateToMainScreens: (String) -> Unit,
) {
    val navDialogState = remember { mutableStateOf(false) }

    AboutContent(
        navigateToContract = navigateToContract,
        isPortrait = isPortrait,
        isCompact = isCompact,
        navDialogState = navDialogState,
        navigateToMainScreens = navigateToMainScreens
    )
}

@Composable
fun AboutContent(
    navigateToContract: (Info.Type) -> Unit,
    isPortrait: Boolean,
    isCompact: Boolean,
    navDialogState: MutableState<Boolean>,
    navigateToMainScreens: (String) -> Unit,
) {
    val gradientBrush = Brush.linearGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.tertiary
        )
    )

    ConstraintLayout(
        modifier = Modifier.azureScreen()
    ) {
        val (topBar, motto, appItemSection, infoItemSection) = createRefs()
        val horizontalGuideline = createGuidelineFromEnd(0.36f)
        val verticalGuideline = createGuidelineFromTop(0.4f)

        val topBarModifier: Modifier
        val mottoModifier: Modifier
        val appItemSectionModifier: Modifier
        val infoItemSectionModifier: Modifier

        if (isPortrait) {
            topBarModifier = Modifier.constrainAs(topBar) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
            mottoModifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(gradientBrush)
                .constrainAs(motto) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(topBar.bottom)
                    bottom.linkTo(verticalGuideline)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            appItemSectionModifier = Modifier
                .padding(top = 12.dp)
                .constrainAs(appItemSection) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(motto.bottom)
                width = Dimension.fillToConstraints
            }
            infoItemSectionModifier = Modifier.constrainAs(infoItemSection) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(appItemSection.bottom)
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
            mottoModifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(gradientBrush)
                .constrainAs(motto) {
                    start.linkTo(topBar.end)
                    end.linkTo(horizontalGuideline)
                    top.linkTo(parent.top)
                    bottom.linkTo(appItemSection.top)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            appItemSectionModifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .constrainAs(appItemSection) {
                    start.linkTo(topBar.end)
                    end.linkTo(horizontalGuideline)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
            infoItemSectionModifier = Modifier.constrainAs(infoItemSection) {
                start.linkTo(horizontalGuideline)
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
                navDialogState = navDialogState,
                destination = AzureDestination.Main.ABOUT,
                navigateToMainScreens = navigateToMainScreens
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = mottoModifier
        ) {
            Text(
                text = stringResource(R.string.app_motto),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (isPortrait && isCompact) {
            Column(modifier = appItemSectionModifier) {
                val appItemModifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                AboutAppItem(
                    heading = stringResource(R.string.app_version_title),
                    text = stringResource(id = R.string.app_version),
                    modifier = appItemModifier
                )
                AboutAppItem(
                    heading = stringResource(R.string.app_last_update_title),
                    text = stringResource(id = R.string.app_last_update),
                    modifier = appItemModifier
                )
            }
        } else {
            Row(modifier = appItemSectionModifier) {
                val appItemModifier = Modifier.weight(1f)
                AboutAppItem(
                    heading = stringResource(R.string.app_version_title),
                    text = stringResource(id = R.string.app_version),
                    modifier = appItemModifier
                )
                AboutAppItem(
                    heading = stringResource(R.string.app_last_update_title),
                    text = stringResource(id = R.string.app_last_update),
                    modifier = appItemModifier
                )
            }
        }

        Column(modifier = infoItemSectionModifier) {
            val infoItemModifier: Modifier
            if (isPortrait) {
                infoItemModifier = Modifier
                    .padding(vertical = 6.dp)
                    .fillMaxWidth()
                Spacer(modifier = Modifier.padding(top = 12.dp))
            } else {
                infoItemModifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            }
            AboutInfoItem(
                heading = stringResource(R.string.service_title) to Info.Type.SERVICE,
                navigateToInfo = navigateToContract,
                modifier = infoItemModifier
            )
            AboutInfoItem(
                heading = stringResource(R.string.privacy_title) to Info.Type.PRIVACY,
                navigateToInfo = navigateToContract,
                modifier = infoItemModifier
            )
            AboutInfoItem(
                heading = stringResource(R.string.acknowledgement_title) to Info.Type.ACKNOWLEDGEMENTS,
                navigateToInfo = navigateToContract,
                modifier = infoItemModifier
            )
            Spacer(modifier = Modifier.weight(1f))
            SelectionContainer {
                Text(
                    text = stringResource(id = R.string.app_website),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
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
    val navDialogState = remember { mutableStateOf(false) }
    AzureTheme {
        AboutContent({}, true, true, navDialogState, {})
    }
}
