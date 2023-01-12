/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.ui.navigation.AzureTopBar
import com.peter.azure.ui.util.ErrorNotice
import com.peter.azure.ui.util.azureScreen

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToMainScreens: (String) -> Unit,
    navigateToNewGame: () -> Unit,
    navigateToContinueGame: () -> Unit
) {
    val navDialogState = remember { mutableStateOf(false) }
    val isPortrait = LocalConfiguration.current.orientation ==
            Configuration.ORIENTATION_PORTRAIT

    HomeContent(
        uiState = viewModel.uiState.value,
        getGameLevel = viewModel::setGameLevel,
        navigateToNewGame = navigateToNewGame,
        navigateToContinueGame = navigateToContinueGame,
        isPortrait = isPortrait,
        navDialogState = navDialogState,
        navigateToMainScreens = navigateToMainScreens,
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    getGameLevel: (Double) -> Unit,
    navigateToNewGame: () -> Unit,
    navigateToContinueGame: () -> Unit,
    isPortrait: Boolean,
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

                val topBarModifier: Modifier
                val settingModifier: Modifier
                val buttonModifier: Modifier
                val dialSize: Int

                if (isPortrait) {
                    topBarModifier = Modifier.constrainAs(topBar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        width = Dimension.fillToConstraints
                    }
                    settingModifier = Modifier.constrainAs(setting) {
                        top.linkTo(topBar.bottom)
                        bottom.linkTo(button.bottom)
                        centerHorizontallyTo(parent)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    buttonModifier = Modifier
                        .padding(bottom = 32.dp)
                        .width(208.dp)
                        .constrainAs(button) {
                            centerHorizontallyTo(parent)
                            bottom.linkTo(parent.bottom)
                        }
                    dialSize = (screenHeightDp / screenWidthDp * 192 * 0.76f).toInt()
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
                        .width(168.dp)
                        .constrainAs(button) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                    dialSize = (screenWidthDp / screenHeightDp * 192 * 0.68f).toInt()
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
                        GameLevelSetting(
                            fullSize = dialSize.dp,
                            setGameLevel = getGameLevel,
                            dialAngle = uiState.dialAngle
                        )
                    } else {
                        Spacer(modifier = Modifier.size(dialSize.dp))
                    }
                }

                val buttonText: String
                val navigate: () -> Unit
                if (uiState.gameExisted) {
                    buttonText = stringResource(R.string.screen_home_continue_game)
                    navigate = navigateToContinueGame
                } else {
                    buttonText = stringResource(R.string.screen_home_start_game)
                    navigate = navigateToNewGame
                }

                Button(
                    contentPadding = PaddingValues(16.dp),
                    onClick = navigate,
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

//@Preview(name = "Home Screen", showBackground = true)
//@Preview(
//    name = "Home Screen (dark)", showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun HomeScreenPreview() {
//    val viewModel = HomeViewModel()
//    AzureTheme {
//        HomeScreen(viewModel, {}, {})
//    }
//}
