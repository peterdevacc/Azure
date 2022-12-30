package com.peter.azure.ui

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.peter.azure.ui.theme.AzureTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

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
                val navHostController = rememberNavController()

                MainContent(
                    uiState = mainViewModel.uiState.value,
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

