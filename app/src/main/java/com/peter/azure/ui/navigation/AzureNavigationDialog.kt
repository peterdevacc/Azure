/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.peter.azure.ui.theme.AzureTheme
import com.peter.azure.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AzureNavigationDialog(
    isPortrait: Boolean,
    currentMainDestination: AzureDestination.Main,
    navigateToMainScreens: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val mainDestinationList = AzureDestination.Main
        .getNavigationList(currentMainDestination)

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss
    ) {
        if (isPortrait) {
            LazyColumn(
                modifier = Modifier
                    .background(Color.Transparent)
                    .heightIn(156.dp, 456.dp)
            ) {
                getMainDestinationListItems(
                    itemSize = 256 to 138,
                    mainDestinationList = mainDestinationList,
                    navigateToMainScreens = navigateToMainScreens,
                    onDismiss = onDismiss
                )
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(4.dp),
                modifier = Modifier
                    .background(Color.Transparent)
                    .widthIn(288.dp, 636.dp)
            ) {
                getMainDestinationListItems(
                    itemSize = 198 to 208,
                    mainDestinationList = mainDestinationList,
                    navigateToMainScreens = navigateToMainScreens,
                    onDismiss = onDismiss
                )
            }
        }
    }
}

private fun LazyListScope.getMainDestinationListItems(
    itemSize: Pair<Int, Int>,
    mainDestinationList: List<AzureDestination.Main>,
    navigateToMainScreens: (String) -> Unit,
    onDismiss: () -> Unit
) = items(mainDestinationList) { destination ->
    val destinationTitle = stringResource(destination.textId)
    val destinationTitleDescription = stringResource(
        R.string.navigation_dialog_item_description,
        destinationTitle
    )
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(4.dp)
            .width(itemSize.first.dp)
            .height(itemSize.second.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                onDismiss()
                navigateToMainScreens(destination.route)
            }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize()
        ) {
            val (title, text) = createRefs()

            Text(
                text = destinationTitle,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .constrainAs(title) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        width = Dimension.fillToConstraints
                    }
                    .padding(bottom = 4.dp)
                    .semantics {
                        this.text = AnnotatedString(
                            destinationTitleDescription
                        )
                    }
            )
            Text(
                text = stringResource(destination.destinationDescriptionId),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .constrainAs(text) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(title.bottom)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AzureNavigationDialogPreview() {
    AzureTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AzureNavigationDialog(
                true,
                AzureDestination.Main.HOME, {}, {}
            )
        }
    }
}
