package com.peter.azure.viewmodel

import com.peter.azure.data.util.DataResult
import com.peter.azure.data.entity.Info
import com.peter.azure.data.repository.InfoRepository
import com.peter.azure.data.repository.PreferencesRepository
import com.peter.azure.ui.greeting.GreetingUiState
import com.peter.azure.ui.greeting.GreetingViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GreetingViewModelTest {

    private val preferencesRepository = mockk<PreferencesRepository>(relaxed = true)
    private val infoRepository = mockk<InfoRepository>(relaxed = true)
    private val viewModel = GreetingViewModel(
        preferencesRepository, infoRepository
    )

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val magicNum = 600L

    private val type = Info.Type.SERVICE
    private val serviceInfo = Info(
        type,
        listOf(
            Info.Item(Info.Item.Type.TITLE, "${type.name} TITLE"),
            Info.Item(Info.Item.Type.TEXT, "${type.name} TEXT"),
            Info.Item(Info.Item.Type.SIGNATURE, "${type.name} SIGNATURE")
        )
    )

    @Test
    fun `load info success`() = runBlocking {
        launch(Dispatchers.Main) {
            coEvery {
                infoRepository.getInfo(type)
            } returns DataResult.Success(serviceInfo)

            viewModel.loadInfo(type)

            delay(magicNum)

            coVerify(exactly = 1) {
                infoRepository.getInfo(type)
            }
            confirmVerified(infoRepository)
            assertTrue(viewModel.uiState.value is GreetingUiState.InfoDialogLoaded)
        }
        Unit
    }

    @Test
    fun `load info error`() = runBlocking {
        launch(Dispatchers.Main) {
            coEvery {
                infoRepository.getInfo(type)
            } returns DataResult.Error(DataResult.Error.Code.UNKNOWN)

            viewModel.loadInfo(type)

            delay(magicNum)

            coVerify(exactly = 1) {
                infoRepository.getInfo(type)
            }
            confirmVerified(infoRepository)
            assertTrue(viewModel.uiState.value is GreetingUiState.Error)
            val result = (viewModel.uiState.value as GreetingUiState.Error).code
            assertEquals(DataResult.Error.Code.UNKNOWN, result)
        }
        Unit
    }

    @Test
    fun `agree info success`() = runBlocking {
        launch(Dispatchers.Main) {
            coEvery {
                preferencesRepository.setOnBoardingState(true)
            } returns DataResult.Success("")

            viewModel.acceptContracts()

            delay(magicNum)

            coVerify(exactly = 1) {
                preferencesRepository.setOnBoardingState(true)
            }
            confirmVerified(preferencesRepository)
            assertTrue(viewModel.uiState.value is GreetingUiState.InfoAccepted)
        }
        Unit
    }

    @Test
    fun `agree info error`() = runBlocking {
        launch(Dispatchers.Main) {
            coEvery {
                preferencesRepository.setOnBoardingState(true)
            } returns DataResult.Error(DataResult.Error.Code.UNKNOWN)

            viewModel.acceptContracts()

            delay(magicNum)

            coVerify(exactly = 1) {
                preferencesRepository.setOnBoardingState(true)
            }
            confirmVerified(preferencesRepository)
            assertTrue(viewModel.uiState.value is GreetingUiState.Error)
            val result = (viewModel.uiState.value as GreetingUiState.Error).code
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