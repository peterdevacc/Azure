/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.peter.azure.ui.about.AboutScreen
import com.peter.azure.ui.info.InfoScreen
import com.peter.azure.ui.info.InfoViewModel
import com.peter.azure.ui.game.GameScreen
import com.peter.azure.ui.game.GameViewModel
import com.peter.azure.ui.greeting.GreetingScreen
import com.peter.azure.ui.greeting.GreetingViewModel
import com.peter.azure.ui.help.HelpScreen
import com.peter.azure.ui.help.HelpViewModel
import com.peter.azure.ui.home.HomeScreen
import com.peter.azure.ui.home.HomeViewModel
import com.peter.azure.ui.print.PrintScreen
import com.peter.azure.ui.print.PrintViewModel

@Composable
fun AzureNavigationGraph(
    isPortrait: Boolean,
    isCompact: Boolean,
    navHostController: NavHostController,
    startDestination: String,
    exitApp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(AzureDestination.Main.HOME.route) {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                viewModel = viewModel,
                navigateToNewGame = {
                    val level = viewModel.getGameLevel()
                    navHostController.navigate(
                        AzureDestination.Main.HOME.getNavGameRoute(level)
                    ) {
                        popUpTo(AzureDestination.Main.HOME.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToContinueGame = {
                    navHostController.navigate(
                        AzureDestination.Main.HOME.getNavGameRoute()
                    ) {
                        popUpTo(AzureDestination.Main.HOME.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                isPortrait = isPortrait,
                isCompact = isCompact,
                navigateToMainScreens = {
                    navHostController.navigate(it) {
                        popUpTo(AzureDestination.Main.HOME.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
        composable(AzureDestination.Main.PRINT.route) {
            val viewModel = hiltViewModel<PrintViewModel>()
            PrintScreen(
                viewModel = viewModel,
                isPortrait = isPortrait,
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
                isPortrait = isPortrait,
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
                navigateToContract = {
                    navHostController.navigate(
                        AzureDestination.Main.ABOUT.getNavContractRoute(it)
                    ) {
                        launchSingleTop = true
                    }
                },
                isPortrait = isPortrait,
                isCompact = isCompact,
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
        composable(
            route = AzureDestination.General.GAME.destRoute,
            arguments = AzureDestination.General.GAME.navArguments
        ) {
            val viewModel = hiltViewModel<GameViewModel>()
            GameScreen(
                viewModel = viewModel,
                isPortrait = isPortrait,
                isCompact = isCompact,
                navigateUp = {
                    navHostController.navigateUp()
                }
            )
        }
        composable(AzureDestination.General.INFO.destRoute) {
            val viewModel = hiltViewModel<InfoViewModel>()
            InfoScreen(
                viewModel = viewModel,
                isPortrait = isPortrait,
                navigateUp = {
                    navHostController.navigateUp()
                }
            )
        }
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
                exitApp = exitApp,
                isPortrait = isPortrait,
                isCompact = isCompact,
            )
        }
    }
}
