/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.help

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.peter.azure.data.entity.Help
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.ui.navigation.AzureTopBar
import com.peter.azure.ui.util.ErrorNotice
import com.peter.azure.ui.util.azureScreen

@Composable
fun HelpScreen(
    viewModel: HelpViewModel,
    isPortrait: Boolean,
    navigateToMainScreens: (String) -> Unit
) {
    val navDialogState = remember { mutableStateOf(false) }

    HelpContent(
        uiState = viewModel.uiState.value,
        isPortrait = isPortrait,
        navDialogState = navDialogState,
        navigateToMainScreens = navigateToMainScreens
    )
}

@Composable
fun HelpContent(
    uiState: HelpUiState,
    isPortrait: Boolean,
    navDialogState: MutableState<Boolean>,
    navigateToMainScreens: (String) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier.azureScreen()
    ) {
        val (topBar, helpContainer) = createRefs()
        val topBarModifier: Modifier
        val helpContainerModifier: Modifier

        if (isPortrait) {
            topBarModifier = Modifier.constrainAs(topBar) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
            helpContainerModifier = Modifier.constrainAs(helpContainer) {
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
            helpContainerModifier = Modifier.constrainAs(helpContainer) {
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
                navDialogState = navDialogState,
                destination = AzureDestination.Main.HELP,
                navigateToMainScreens = navigateToMainScreens
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = helpContainerModifier
        ) {
            when (uiState) {
                is HelpUiState.Error -> {
                    ErrorNotice(uiState.code)
                }
                is HelpUiState.Success -> {
                    HelpList(isPortrait, uiState.helpMap)
                }
                is HelpUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HelpList(
    isPortrait: Boolean,
    helpMap: Map<Help.Catalog, List<Help>>
) {
    if (isPortrait) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            getHelpItems(true, helpMap)
        }
    } else {
        LazyRow(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxSize()
        ) {
            getHelpItems(false, helpMap)
        }
    }
}

private fun LazyListScope.getHelpItems(
    isPortrait: Boolean,
    helpMap: Map<Help.Catalog, List<Help>>
) = items(helpMap.toList()) { (catalog, helpList) ->
    val colorPair = getHelpItemColorPair(catalog)
    val itemModifier: Modifier
    val spacerModifier: Modifier

    if (isPortrait) {
        itemModifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(colorPair.first)
            .padding(16.dp)
        spacerModifier = Modifier.padding(vertical = 8.dp)
    } else {
        itemModifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(colorPair.first)
            .padding(16.dp)
            .width(320.dp)
            .fillMaxHeight()
        spacerModifier = Modifier.padding(horizontal = 8.dp)
    }

    Column(modifier = itemModifier) {
        Text(
            text = catalog.name,
            style = MaterialTheme.typography.titleLarge,
            color = colorPair.second.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (isPortrait) {
            helpList.forEach { help ->
                HelpItem(
                    help = help,
                    colorPair = colorPair,
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 4.dp)
                        .fillMaxWidth()
                )
            }
        } else {
            LazyColumn {
                items(helpList) { help ->
                    HelpItem(
                        help = help,
                        colorPair = colorPair,
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 4.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }

    Spacer(modifier = spacerModifier)
}

@Composable
private fun HelpItem(
    help: Help,
    colorPair: Pair<Color, Color>,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = help.title,
            style = MaterialTheme.typography.titleMedium,
            color = colorPair.second,
            modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
        )
        Text(
            text = help.text,
            style = MaterialTheme.typography.bodyLarge,
            color = colorPair.second,
            modifier = Modifier
                .padding(vertical = 2.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun getHelpItemColorPair(catalog: Help.Catalog): Pair<Color, Color> {
    return when (catalog) {
        Help.Catalog.FAQ -> Pair(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            MaterialTheme.colorScheme.onPrimaryContainer
        )
        Help.Catalog.TUTORIAL -> Pair(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

//@Preview(name = "Help Screen", showBackground = true)
//@Preview(
//    name = "Help Screen (dark)", showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun HelpScreenPreview() {
//    val viewModel = HelpViewModel()
//    val navHostController = rememberNavController()
//    AzureTheme {
//        HelpScreen(viewModel, navHostController)
//    }
//}
