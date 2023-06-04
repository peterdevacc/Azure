/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peter.azure.R
import com.peter.azure.data.entity.GameLevel
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.ui.util.AzureTopBar
import com.peter.azure.ui.util.ErrorNotice
import com.peter.azure.ui.util.azureScreen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToNewGame: () -> Unit,
    navigateToContinueGame: () -> Unit,
    isCompact: Boolean,
    isPortrait: Boolean,
    navigateToMainScreens: (String) -> Unit,
) {
    val uiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    HomeContent(
        uiState = uiState,
        setGameLevel = viewModel::setGameLevel,
        getGameLevel = viewModel::getGameLevel,
        navigateToNewGame = navigateToNewGame,
        navigateToContinueGame = navigateToContinueGame,
        isPortrait = isPortrait,
        isCompact = isCompact,
        navigateToMainScreens = navigateToMainScreens,
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    setGameLevel: (Int) -> Unit,
    getGameLevel: () -> GameLevel,
    navigateToNewGame: () -> Unit,
    navigateToContinueGame: () -> Unit,
    isPortrait: Boolean,
    isCompact: Boolean,
    navigateToMainScreens: (String) -> Unit,
) {

    when (uiState) {
        is HomeUiState.Success -> {
            val scope = rememberCoroutineScope()

            ConstraintLayout(
                modifier = Modifier.azureScreen()
            ) {
                val (topBar, setting, button) = createRefs()
                val screenWidthDp = LocalConfiguration.current.screenWidthDp.toFloat()
                val screenHeightDp = LocalConfiguration.current.screenHeightDp.toFloat()
                val minSide = minOf(screenWidthDp, screenHeightDp)

                val topBarModifier: Modifier
                val settingModifier: Modifier
                val continueButtonModifier: Modifier
                val gameLevelButtonGroupModifier: Modifier
                val blockSize = if (isCompact) {
                    (minSide * 0.73f).toInt()
                } else {
                    (minSide * 0.52f).toInt()
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
                    continueButtonModifier = Modifier
                        .padding(bottom = 32.dp)
                        .width((blockSize * 0.53f).toInt().dp)
                        .constrainAs(button) {
                            centerHorizontallyTo(parent)
                            bottom.linkTo(parent.bottom)
                        }
                    gameLevelButtonGroupModifier = Modifier
                        .padding(bottom = 32.dp)
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
                    continueButtonModifier = Modifier
                        .width((blockSize * 0.53f).toInt().dp)
                        .constrainAs(button) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                    gameLevelButtonGroupModifier = Modifier
                        .padding(end = 8.dp)
                        .constrainAs(button) {
                            end.linkTo(parent.end)
                            centerVerticallyTo(parent)
                        }
                }

                Box(modifier = topBarModifier) {
                    AzureTopBar(
                        isPortrait = isPortrait,
                        destination = AzureDestination.Main.HOME,
                        navigateToMainScreens = navigateToMainScreens
                    )
                }

                val anim = remember { Animatable(0f) }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = settingModifier
                ) {
                    if (!uiState.gameExisted) {
                        val level = getGameLevel()
                        val description = stringResource(
                            R.string.game_level_block_description,
                            level.name
                        )

                        LaunchedEffect(anim) {
                            anim.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(
                                    durationMillis = 600,
                                    easing = LinearEasing
                                )
                            )
                        }

                        GameLevelBlock(
                            fullSize = blockSize.dp,
                            gameLevel = level,
                            semanticsDescription = description,
                            anim = anim.value
                        )
                    } else {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(blockSize.dp)
                        ) {
                            AzureBlock(fullSize = (blockSize * 0.76f).dp)
                        }
                    }
                }

                if (uiState.gameExisted) {
                    Button(
                        onClick = navigateToContinueGame,
                        contentPadding = PaddingValues(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = continueButtonModifier
                    ) {
                        Text(
                            text = stringResource(R.string.screen_home_continue_game),
                            fontSize = 18.sp,
                        )
                    }
                } else {
                    Box(
                        modifier = gameLevelButtonGroupModifier
                    ) {
                        GameLevelButtonGroup(
                            isPortrait = isPortrait,
                            currentGameLevel = uiState.gameLevel,
                            setGameLevel = { level ->
                                setGameLevel(level)
                                scope.launch {
                                    anim.snapTo(0f)
                                    anim.animateTo(
                                        targetValue = 1f,
                                        animationSpec = tween(
                                            durationMillis = 600,
                                            easing = LinearEasing
                                        )
                                    )
                                }
                            },
                            startGame = navigateToNewGame,
                        )
                    }
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
