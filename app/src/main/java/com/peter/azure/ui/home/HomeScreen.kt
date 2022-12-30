package com.peter.azure.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.ui.navigation.AzureTopBar
import com.peter.azure.ui.util.ErrorNotice
import com.peter.azure.ui.util.azureScreen
import com.peter.azure.R

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToMainScreens: (String) -> Unit,
    navigateToNewGame: () -> Unit,
    navigateToContinueGame: () -> Unit
) {
    val navDialogState = remember { mutableStateOf(false) }

    HomeContent(
        uiState = viewModel.uiState.value,
        navDialogState = navDialogState,
        navigateToMainScreens = navigateToMainScreens,
        getGameLevel = viewModel::getGameLevel,
        navigateToNewGame = navigateToNewGame,
        navigateToContinueGame = navigateToContinueGame
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    navDialogState: MutableState<Boolean>,
    navigateToMainScreens: (String) -> Unit,
    getGameLevel: (Double) -> Unit,
    navigateToNewGame: () -> Unit,
    navigateToContinueGame: () -> Unit
) {

    when (uiState) {
        is HomeUiState.Error -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                ErrorNotice(uiState.code)
            }
        }
        is HomeUiState.Success -> {
            Column(
                modifier = Modifier.azureScreen()
            ) {
                AzureTopBar(
                    navDialogState = navDialogState,
                    destination = AzureDestination.Main.HOME,
                    navigateToMainScreens = navigateToMainScreens
                )
                Spacer(modifier = Modifier.weight(5f))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!uiState.gameExisted) {
                        GameLevelSetting(
                            getGameLevel = getGameLevel,
                            fullSize = 288.dp,
                        )
                    } else {
                        Spacer(modifier = Modifier.size(288.dp))
                    }
                    Spacer(modifier = Modifier.padding(bottom = 24.dp))
                    val buttonText = if (uiState.gameExisted) {
                        stringResource(R.string.screen_home_continue_game)
                    } else {
                        stringResource(R.string.screen_home_start_game)
                    }
                    val navigate = if (uiState.gameExisted) {
                        navigateToContinueGame
                    } else {
                        navigateToNewGame
                    }
                    Button(
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .width(260.dp),
                        onClick = navigate
                    ) {
                        Text(
                            text = buttonText,
                            fontSize = 16.sp,
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(2f))
            }
        }
        is HomeUiState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp, 24.dp)
                )
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
