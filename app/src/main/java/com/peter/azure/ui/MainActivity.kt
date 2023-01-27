/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.peter.azure.ui.theme.AzureTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            mainViewModel.uiState.value == MainUiState.Loading
        }

        WindowCompat.setDecorFitsSystemWindows(
            window, false
        )

        setContent {
            AzureTheme {
                val windowSizeClass = calculateWindowSizeClass(activity = this@MainActivity)
                val isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact ||
                        windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
                val isPortrait = LocalConfiguration.current.orientation ==
                        Configuration.ORIENTATION_PORTRAIT
                val navHostController = rememberNavController()

                MainContent(
                    uiState = mainViewModel.uiState.value,
                    isPortrait = isPortrait,
                    isCompact = isCompact,
                    navHostController = navHostController,
                    finish = this@MainActivity::finish
                )
            }
        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return ev?.pointerCount == 1 && super.dispatchTouchEvent(ev)
    }

}

