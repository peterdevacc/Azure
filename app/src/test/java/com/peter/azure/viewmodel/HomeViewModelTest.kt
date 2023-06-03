package com.peter.azure.viewmodel

import com.peter.azure.data.entity.GameLevel
import com.peter.azure.data.repository.PreferencesRepository
import com.peter.azure.data.util.DataResult
import com.peter.azure.ui.home.HomeUiState
import com.peter.azure.ui.home.HomeViewModel
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
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

        val job = launch(Dispatchers.Main, CoroutineStart.LAZY) {
            viewModel.homeUiState.collect()
        }
        job.start()

        delay(magicNum)

        viewModel.setDialAngle(expected)
        delay(magicNum)
        val result = (viewModel.homeUiState.value as HomeUiState.Success).dialAngle
        val delta = 1e-15
        assertEquals(expected, result, delta)

        job.cancelAndJoin()
    }

    @Test
    fun `get game level`() = runBlocking {
        coEvery {
            preferencesRepository.getGameExistedState()
        } returns flowOf(
            DataResult.Success(false)
        )

        val viewModel = HomeViewModel(preferencesRepository)

        val job = launch(Dispatchers.Main, CoroutineStart.LAZY) {
            viewModel.homeUiState.collect()
        }
        job.start()

        delay(magicNum)

        viewModel.setDialAngle(-130.0)
        delay(magicNum)
        var level = viewModel.getGameLevel()
        assertEquals(GameLevel.EASY, level)

        viewModel.setDialAngle(30.0)
        delay(magicNum)
        level = viewModel.getGameLevel()
        assertEquals(GameLevel.MODERATE, level)

        viewModel.setDialAngle(-30.0)
        delay(magicNum)
        level = viewModel.getGameLevel()
        assertEquals(GameLevel.HARD, level)

        job.cancelAndJoin()

    }

    @Test
    fun `init success`() = runBlocking {
        // game not existed
        coEvery {
            preferencesRepository.getGameExistedState()
        } returns flowOf(
            DataResult.Success(false)
        )
        var viewModel = HomeViewModel(preferencesRepository)

        var job = launch(Dispatchers.Main, CoroutineStart.LAZY) {
            viewModel.homeUiState.collect()
        }
        job.start()

        delay(magicNum)
        coVerify(exactly = 1) {
            preferencesRepository.getGameExistedState()
        }
        confirmVerified(preferencesRepository)
        assertTrue(viewModel.homeUiState.value is HomeUiState.Success)
        var result = (viewModel.homeUiState.value as HomeUiState.Success).gameExisted
        assertEquals(false, result)
        clearAllMocks()

        job.cancelAndJoin()

        // game existed
        coEvery {
            preferencesRepository.getGameExistedState()
        } returns flowOf(
            DataResult.Success(true)
        )
        viewModel = HomeViewModel(preferencesRepository)

        job = launch(Dispatchers.Main, CoroutineStart.LAZY) {
            viewModel.homeUiState.collect()
        }
        job.start()

        delay(magicNum)
        coVerify(exactly = 1) {
            preferencesRepository.getGameExistedState()
        }
        confirmVerified(preferencesRepository)
        assertTrue(viewModel.homeUiState.value is HomeUiState.Success)
        result = (viewModel.homeUiState.value as HomeUiState.Success).gameExisted
        assertEquals(true, result)

        job.cancelAndJoin()
    }

    @Test
    fun `init error`() = runBlocking {
        coEvery {
            preferencesRepository.getGameExistedState()
        } returns flowOf(
            DataResult.Error(DataResult.Error.Code.UNKNOWN)
        )
        val viewModel = HomeViewModel(preferencesRepository)

        val job = launch(Dispatchers.Main, CoroutineStart.LAZY) {
            viewModel.homeUiState.collect()
        }
        job.start()

        delay(magicNum)

        coVerify(exactly = 1) {
            preferencesRepository.getGameExistedState()
        }

        confirmVerified(preferencesRepository)
        assertTrue(viewModel.homeUiState.value is HomeUiState.Error)
        val result = (viewModel.homeUiState.value as HomeUiState.Error).code
        assertEquals(DataResult.Error.Code.UNKNOWN, result)

        job.cancelAndJoin()
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