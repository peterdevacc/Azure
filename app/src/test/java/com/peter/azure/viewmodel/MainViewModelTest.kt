package com.peter.azure.viewmodel

import com.peter.azure.data.util.DataResult
import com.peter.azure.data.repository.PreferencesRepository
import com.peter.azure.ui.MainUiState
import com.peter.azure.ui.MainViewModel
import com.peter.azure.ui.navigation.AzureDestination
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private val preferencesRepository = mockk<PreferencesRepository>(relaxed = true)

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val magicNum = 600L

    @Test
    fun `init success`() = runBlocking {
        launch(Dispatchers.Main) {
            // completed onBoarding
            coEvery {
                preferencesRepository.getOnBoardingState()
            } returns DataResult.Success(true)
            var viewModel = MainViewModel(preferencesRepository)
            delay(magicNum)
            coVerify(exactly = 1) {
                preferencesRepository.getOnBoardingState()
            }
            confirmVerified(preferencesRepository)
            assertTrue(viewModel.uiState.value is MainUiState.Success)
            var result = (viewModel.uiState.value as MainUiState.Success).startDestination
            assertEquals(AzureDestination.Main.HOME.route, result)
            clearAllMocks()

            // not completed onBoarding
            coEvery {
                preferencesRepository.getOnBoardingState()
            } returns DataResult.Success(false)
            viewModel = MainViewModel(preferencesRepository)
            delay(magicNum)
            coVerify(exactly = 1) {
                preferencesRepository.getOnBoardingState()
            }
            confirmVerified(preferencesRepository)
            assertTrue(viewModel.uiState.value is MainUiState.Success)
            result = (viewModel.uiState.value as MainUiState.Success).startDestination
            assertEquals(AzureDestination.General.GREETING.route, result)
        }
        Unit
    }

    @Test
    fun `init error`() = runBlocking {
        launch(Dispatchers.Main) {
            coEvery {
                preferencesRepository.getOnBoardingState()
            } returns DataResult.Error(DataResult.Error.Code.UNKNOWN)

            val viewModel = MainViewModel(preferencesRepository)

            delay(magicNum)

            coVerify(exactly = 1) {
                preferencesRepository.getOnBoardingState()
            }
            confirmVerified(preferencesRepository)
            assertTrue(viewModel.uiState.value is MainUiState.Error)
            val result = (viewModel.uiState.value as MainUiState.Error).code
            assertEquals(DataResult.Error.Code.UNKNOWN, result)
        }
        Unit
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun clean() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

}