package com.peter.azure.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.peter.azure.ui.about.AboutScreen
import com.peter.azure.ui.contract.ContractScreen
import com.peter.azure.ui.contract.ContractViewModel
import com.peter.azure.ui.game.GameScreen
import com.peter.azure.ui.game.GameViewModel
import com.peter.azure.ui.greeting.GreetingScreen
import com.peter.azure.ui.greeting.GreetingViewModel
import com.peter.azure.ui.help.HelpScreen
import com.peter.azure.ui.help.HelpViewModel
import com.peter.azure.ui.home.HomeScreen
import com.peter.azure.ui.home.HomeUiState
import com.peter.azure.ui.home.HomeViewModel
import com.peter.azure.ui.print.PrintScreen
import com.peter.azure.ui.print.PrintViewModel

@Composable
fun AzureNavigationGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    startDestination: String,
    exitApp: () -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(AzureDestination.General.GREETING.route) {
            val viewModel = hiltViewModel<GreetingViewModel>()
            GreetingScreen(
                viewModel = viewModel,
                navigateToHome = {
                    navHostController.navigate(AzureDestination.Main.HOME.route) {
                        popUpTo(AzureDestination.General.GREETING.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                exitApp = exitApp
            )
        }
        composable(AzureDestination.Main.HOME.route) {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                viewModel = viewModel,
                navigateToMainScreens = {
                    navHostController.navigate(it) {
                        popUpTo(AzureDestination.Main.HOME.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToNewGame = {
                    if (viewModel.uiState.value is HomeUiState.Success) {
                        val level = viewModel.getGameLevel()
                        val route = "${AzureDestination.General.GAME.route}/$level"
                        navHostController.navigate(route) {
                            popUpTo(AzureDestination.Main.HOME.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                navigateToContinueGame = {
                    if (viewModel.uiState.value is HomeUiState.Success) {
                        val route = "${AzureDestination.General.GAME.route}/empty"
                        navHostController.navigate(route) {
                            popUpTo(AzureDestination.Main.HOME.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
        composable(AzureDestination.Main.PRINT.route) {
            val viewModel = hiltViewModel<PrintViewModel>()
            PrintScreen(
                viewModel = viewModel,
                navigateToMainScreens = {
                    navHostController.navigate(it) {
                        popUpTo(AzureDestination.Main.HOME.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(AzureDestination.Main.HELP.route) {
            val viewModel = hiltViewModel<HelpViewModel>()
            HelpScreen(
                viewModel = viewModel,
                navigateToMainScreens = {
                    navHostController.navigate(it) {
                        popUpTo(AzureDestination.Main.HOME.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(AzureDestination.Main.ABOUT.route) {
            AboutScreen(
                navigateToMainScreens = {
                    navHostController.navigate(it) {
                        popUpTo(AzureDestination.Main.HOME.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToContract = { infoType ->
                    navHostController.navigate(
                        "${AzureDestination.General.CONTRACT.route}/$infoType"
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = "${AzureDestination.General.GAME.route}/{gameLevel}"
        ) {
            val viewModel = hiltViewModel<GameViewModel>()
            GameScreen(
                viewModel = viewModel,
                navigateUp = {
                    navHostController.navigateUp()
                }
            )
        }
        composable(
            route = "${AzureDestination.General.CONTRACT.route}/{infoType}",
        ) {
            val viewModel = hiltViewModel<ContractViewModel>()
            ContractScreen(
                viewModel = viewModel,
                navigateUp = {
                    navHostController.navigateUp()
                }
            )
        }

    }
}
