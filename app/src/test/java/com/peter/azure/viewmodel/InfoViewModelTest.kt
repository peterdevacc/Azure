package com.peter.azure.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.peter.azure.data.util.DataResult
import com.peter.azure.data.entity.Info
import com.peter.azure.data.repository.InfoRepository
import com.peter.azure.data.util.INFO_TYPE_SAVED_KEY
import com.peter.azure.ui.info.InfoUiState
import com.peter.azure.ui.info.InfoViewModel
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class InfoViewModelTest {

    private val infoRepository = mockk<InfoRepository>(relaxed = true)

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
    fun `init success`() = runBlocking {
        launch(Dispatchers.Main) {
            coEvery {
                infoRepository.getInfo(type)
            } returns DataResult.Success(serviceInfo)

            val savedStateHandle = SavedStateHandle(
                mapOf(INFO_TYPE_SAVED_KEY to type.name)
            )
            val viewModel = InfoViewModel(infoRepository, savedStateHandle)

            delay(magicNum)

            assertTrue(viewModel.uiState.value is InfoUiState.Success)
            coVerify(exactly = 1) {
                infoRepository.getInfo(type)
            }
            confirmVerified(infoRepository)
        }
        Unit
    }

    @Test
    fun `init error`() = runBlocking {
        launch(Dispatchers.Main) {
            // infoRepository.getInfo error
            coEvery {
                infoRepository.getInfo(any())
            } returns DataResult.Error(DataResult.Error.Code.IO)
            val savedStateHandle = SavedStateHandle(
                mapOf(INFO_TYPE_SAVED_KEY to type.name)
            )
            val viewModel = InfoViewModel(infoRepository, savedStateHandle)
            delay(magicNum)
            assertTrue(viewModel.uiState.value is InfoUiState.Error)
            coVerify(exactly = 1) {
                infoRepository.getInfo(type)
            }
            confirmVerified(infoRepository)
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