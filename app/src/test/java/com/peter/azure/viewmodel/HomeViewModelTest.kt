package com.peter.azure.viewmodel

import com.peter.azure.data.entity.DataResult
import com.peter.azure.data.entity.GameLevel
import com.peter.azure.data.repository.PreferencesRepository
import com.peter.azure.ui.home.HomeUiState
import com.peter.azure.ui.home.HomeViewModel
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

    private val preferencesRepository = mockk<PreferencesRepository>(relaxed = true)

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val magicNum = 600L

    @Test
    fun `set dial angle`() = runBlocking {
        val expected = 30.0
        coEvery {
            preferencesRepository.getGameExistedState()
        } returns flowOf(
            DataResult.Success(false)
        )

        val viewModel = HomeViewModel(preferencesRepository)

        delay(magicNum)

        viewModel.setDialAngle(expected)
        val result = (viewModel.uiState.value as HomeUiState.Success).dialAngle
        val delta = 1e-15
        assertEquals(expected, result, delta)
    }

    @Test
    fun `get game level`() = runBlocking {
        coEvery {
            preferencesRepository.getGameExistedState()
        } returns flowOf(
            DataResult.Success(false)
        )

        val viewModel = HomeViewModel(preferencesRepository)

        delay(magicNum)

        viewModel.setDialAngle(-130.0)
        var level = viewModel.getGameLevel()
        assertEquals(GameLevel.EASY, level)

        viewModel.setDialAngle(30.0)
        level = viewModel.getGameLevel()
        assertEquals(GameLevel.MODERATE, level)

        viewModel.setDialAngle(-30.0)
        level = viewModel.getGameLevel()
        assertEquals(GameLevel.HARD, level)
    }

    @Test
    fun `init success`() = runBlocking {
        launch(Dispatchers.Main) {
            // game not existed
            coEvery {
                preferencesRepository.getGameExistedState()
            } returns flowOf(
                DataResult.Success(false)
            )
            var viewModel = HomeViewModel(preferencesRepository)
            delay(magicNum)
            coVerify(exactly = 1) {
                preferencesRepository.getGameExistedState()
            }
            confirmVerified(preferencesRepository)
            assertTrue(viewModel.uiState.value is HomeUiState.Success)
            var result = (viewModel.uiState.value as HomeUiState.Success).gameExisted
            assertEquals(false, result)
            clearAllMocks()

            // game existed
            coEvery {
                preferencesRepository.getGameExistedState()
            } returns flowOf(
                DataResult.Success(true)
            )
            viewModel = HomeViewModel(preferencesRepository)
            delay(magicNum)
            coVerify(exactly = 1) {
                preferencesRepository.getGameExistedState()
            }
            confirmVerified(preferencesRepository)
            assertTrue(viewModel.uiState.value is HomeUiState.Success)
            result = (viewModel.uiState.value as HomeUiState.Success).gameExisted
            assertEquals(true, result)
        }
        Unit
    }

    @Test
    fun `init error`() = runBlocking {
        launch(Dispatchers.Main) {
            coEvery {
                preferencesRepository.getGameExistedState()
            } returns flowOf(
                DataResult.Error(DataResult.Error.Code.UNKNOWN)
            )
            val viewModel = HomeViewModel(preferencesRepository)

            delay(magicNum)

            coVerify(exactly = 1) {
                preferencesRepository.getGameExistedState()
            }

            confirmVerified(preferencesRepository)
            assertTrue(viewModel.uiState.value is HomeUiState.Error)
            val result = (viewModel.uiState.value as HomeUiState.Error).code
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