/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.util

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.peter.azure.R
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.ui.navigation.AzureNavigationDialog
import com.peter.azure.ui.theme.azureShapes

@Composable
fun AzureTopBar(
    isPortrait: Boolean,
    destination: AzureDestination.Main,
    navigateToMainScreens: (String) -> Unit,
) {
    val navDialogState = remember { mutableStateOf(false) }

    if (navDialogState.value) {
        AzureNavigationDialog(
            isPortrait = isPortrait,
            currentMainDestination = destination,
            navigateToMainScreens = navigateToMainScreens,
            onDismiss = { navDialogState.value = false }
        )
    }

    val containerModifier = if (isPortrait) {
        Modifier
            .padding(top = 8.dp, bottom = 22.dp)
            .fillMaxWidth()
    } else {
        Modifier.fillMaxHeight()
    }

    val destinationText = stringResource(destination.textId)
    val screenHeadingDescription = stringResource(
        R.string.screen_heading_description, destinationText
    )

    ConstraintLayout(
        modifier = containerModifier
    ) {
        val (heading, icon) = createRefs()
        val headingModifier: Modifier
        val buttonModifier: Modifier

        if (isPortrait) {
            headingModifier = Modifier.constrainAs(heading) {
                start.linkTo(parent.start)
                centerVerticallyTo(parent)
            }
            buttonModifier = Modifier.constrainAs(icon) {
                end.linkTo(parent.end)
                centerVerticallyTo(parent)
            }
        } else {
            headingModifier = Modifier.constrainAs(heading) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            }
            buttonModifier = Modifier.constrainAs(icon) {
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            }
        }

        Text(
            text = destinationText,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = headingModifier.semantics {
                text = AnnotatedString(screenHeadingDescription)
                liveRegion = LiveRegionMode.Assertive
            }
        )

        FilledTonalIconButton(
            onClick = { navDialogState.value = true },
            shape = azureShapes.large,
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ),
            modifier = buttonModifier.size(48.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_menu_24),
                contentDescription = stringResource(R.string.icon_cd_navigation_menu),
                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.size(24.dp)
            )
        }
    }

}

@Composable
fun AzureTopBar(
    isPortrait: Boolean,
    destination: AzureDestination.General,
    navigateUp: () -> Unit,
    leftSideContent: @Composable () -> Unit = {}
) {
    val containerModifier = if (isPortrait) {
        Modifier
            .padding(top = 8.dp, bottom = 12.dp)
            .fillMaxWidth()
    } else {
        Modifier.fillMaxHeight()
    }

    val destinationText = stringResource(destination.textId)
    val screenHeadingDescription = stringResource(
        R.string.screen_heading_description, destinationText
    )

    ConstraintLayout(modifier = containerModifier) {
        val (icon, heading, leftSide) = createRefs()

        val iconModifier: Modifier
        val headingModifier: Modifier
        val leftSideModifier: Modifier

        if (isPortrait) {
            iconModifier = Modifier.constrainAs(icon) {
                start.linkTo(parent.start)
                centerVerticallyTo(parent)
            }
            headingModifier = Modifier.constrainAs(heading) {
                centerHorizontallyTo(parent)
                centerVerticallyTo(parent)
            }
            leftSideModifier = Modifier.constrainAs(leftSide) {
                end.linkTo(parent.end)
                centerVerticallyTo(parent)
            }
        } else {
            iconModifier = Modifier.constrainAs(icon) {
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            }
            headingModifier = Modifier.constrainAs(heading) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            }
            leftSideModifier = Modifier.constrainAs(leftSide) {
                start.linkTo(parent.start)
                top.linkTo(heading.bottom)
            }
        }

        IconButton(
            onClick = navigateUp,
            modifier = iconModifier
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back_24),
                contentDescription = stringResource(R.string.icon_cd_navigate_up),
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }

        Text(
            text = stringResource(destination.textId),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = headingModifier.semantics {
                text = AnnotatedString(screenHeadingDescription)
                liveRegion = LiveRegionMode.Assertive
            }
        )

        Box(modifier = leftSideModifier) {
            leftSideContent()
        }
    }
}
