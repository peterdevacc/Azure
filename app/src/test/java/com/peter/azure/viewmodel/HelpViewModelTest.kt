package com.peter.azure.viewmodel

import com.peter.azure.data.util.DataResult
import com.peter.azure.data.entity.Help
import com.peter.azure.data.repository.HelpRepository
import com.peter.azure.ui.help.HelpUiState
import com.peter.azure.ui.help.HelpViewModel
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

class HelpViewModelTest {

    private val helpRepository = mockk<HelpRepository>(relaxed = true)

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val magicNum = 600L

    private val helpList = listOf(
        Help(Help.Catalog.FAQ, "faq 1", "text for faq 1"),
        Help(Help.Catalog.FAQ, "faq 2", "text for faq 2"),
        Help(Help.Catalog.FAQ, "faq 3", "text for faq 3"),
        Help(Help.Catalog.TUTORIAL, "TUTORIAL 4", "text for TUTORIAL 4"),
        Help(Help.Catalog.TUTORIAL, "TUTORIAL 5", "text for TUTORIAL 5"),
        Help(Help.Catalog.TUTORIAL, "TUTORIAL 6", "text for TUTORIAL 6"),
        Help(Help.Catalog.FAQ, "faq 7", "text for faq 7"),
        Help(Help.Catalog.TUTORIAL, "TUTORIAL 8", "text for TUTORIAL 8"),
        Help(Help.Catalog.FAQ, "faq 9", "text for faq 9"),
    )
    private val expected = helpList.groupBy { it.catalog }

    @Test
    fun `init success`() = runBlocking {
        launch(Dispatchers.Main) {
            coEvery {
                helpRepository.getHelpMap()
            } returns DataResult.Success(expected)

            val viewModel = HelpViewModel(helpRepository)

            delay(magicNum)

            coVerify(exactly = 1) {
                helpRepository.getHelpMap()
            }

            confirmVerified(helpRepository)
            assertTrue(viewModel.uiState.value is HelpUiState.Success)
        }
        Unit
    }

    @Test
    fun `init error`() = runBlocking {
        launch(Dispatchers.Main) {
            coEvery {
                helpRepository.getHelpMap()
            } returns DataResult.Error(DataResult.Error.Code.UNKNOWN)

            val viewModel = HelpViewModel(helpRepository)

            delay(magicNum)

            coVerify(exactly = 1) {
                helpRepository.getHelpMap()
            }

            confirmVerified(helpRepository)
            assertTrue(viewModel.uiState.value is HelpUiState.Error)
            val result = (viewModel.uiState.value as HelpUiState.Error).code
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