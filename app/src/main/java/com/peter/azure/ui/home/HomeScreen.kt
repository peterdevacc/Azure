/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.peter.azure.R
import com.peter.azure.data.entity.GameLevel
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.ui.util.AzureTopBar
import com.peter.azure.ui.util.ErrorNotice
import com.peter.azure.ui.util.azureScreen

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToNewGame: () -> Unit,
    navigateToContinueGame: () -> Unit,
    isCompact: Boolean,
    isPortrait: Boolean,
    navigateToMainScreens: (String) -> Unit,
) {
    val navDialogState = remember { mutableStateOf(false) }

    HomeContent(
        uiState = viewModel.uiState.value,
        setDialAngle = viewModel::setDialAngle,
        getGameLevel = viewModel::getGameLevel,
        navigateToNewGame = navigateToNewGame,
        navigateToContinueGame = navigateToContinueGame,
        isPortrait = isPortrait,
        isCompact = isCompact,
        navDialogState = navDialogState,
        navigateToMainScreens = navigateToMainScreens,
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    setDialAngle: (Double) -> Unit,
    getGameLevel: () -> GameLevel,
    navigateToNewGame: () -> Unit,
    navigateToContinueGame: () -> Unit,
    isPortrait: Boolean,
    isCompact: Boolean,
    navDialogState: MutableState<Boolean>,
    navigateToMainScreens: (String) -> Unit,
) {

    when (uiState) {
        is HomeUiState.Success -> {
            ConstraintLayout(
                modifier = Modifier.azureScreen()
            ) {
                val (topBar, setting, button) = createRefs()
                val screenWidthDp = LocalConfiguration.current.screenWidthDp.toFloat()
                val screenHeightDp = LocalConfiguration.current.screenHeightDp.toFloat()
                val minSide = minOf(screenWidthDp, screenHeightDp)

                val topBarModifier: Modifier
                val settingModifier: Modifier
                val buttonModifier: Modifier
                val dialSize = if (isCompact) {
                    (minSide * 0.78f).toInt()
                } else {
                    (minSide * 0.57f).toInt()
                }

                if (isPortrait) {
                    topBarModifier = Modifier.constrainAs(topBar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        width = Dimension.fillToConstraints
                    }
                    settingModifier = Modifier
                        .constrainAs(setting) {
                            top.linkTo(topBar.bottom)
                            bottom.linkTo(button.top)
                            centerHorizontallyTo(parent)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                    buttonModifier = Modifier
                        .padding(bottom = 32.dp)
                        .width((dialSize * 0.53f).toInt().dp)
                        .constrainAs(button) {
                            centerHorizontallyTo(parent)
                            bottom.linkTo(parent.bottom)
                        }
                } else {
                    topBarModifier = Modifier.constrainAs(topBar) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
                    settingModifier = Modifier.constrainAs(setting) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        centerHorizontallyTo(parent)
                        height = Dimension.fillToConstraints
                    }
                    buttonModifier = Modifier
                        .width((dialSize * 0.53f).toInt().dp)
                        .constrainAs(button) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                }

                Box(modifier = topBarModifier) {
                    AzureTopBar(
                        isPortrait = isPortrait,
                        navDialogState = navDialogState,
                        destination = AzureDestination.Main.HOME,
                        navigateToMainScreens = navigateToMainScreens
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = settingModifier
                ) {
                    if (!uiState.gameExisted) {
                        val level = getGameLevel()
                        val description = stringResource(
                            R.string.game_level_setting_description,
                            level.name
                        )
                        GameLevelWheel(
                            fullSize = dialSize.dp,
                            dialAngle = uiState.dialAngle,
                            setDialAngle = setDialAngle,
                            semanticsDescription = description
                        )
                    } else {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(dialSize.dp)
                        ) {
                            AzureBlock(fullSize = (dialSize * 0.76f).dp)
                        }
                    }
                }

                val buttonText: String
                val navigate: () -> Unit
                val buttonColors: ButtonColors
                if (uiState.gameExisted) {
                    buttonText = stringResource(R.string.screen_home_continue_game)
                    navigate = navigateToContinueGame
                    buttonColors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    buttonText = stringResource(R.string.screen_home_start_game)
                    navigate = navigateToNewGame
                    buttonColors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                }

                Button(
                    onClick = navigate,
                    contentPadding = PaddingValues(16.dp),
                    colors = buttonColors,
                    modifier = buttonModifier
                ) {
                    Text(
                        text = buttonText,
                        fontSize = 18.sp,
                    )
                }
            }
        }
        else -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (uiState is HomeUiState.Error) {
                    ErrorNotice(uiState.code)
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
            }
        }
    }
}
