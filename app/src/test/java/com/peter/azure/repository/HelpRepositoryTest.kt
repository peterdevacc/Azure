package com.peter.azure.repository

import android.content.res.AssetManager
import com.peter.azure.data.entity.DataResult
import com.peter.azure.data.entity.Help
import com.peter.azure.data.repository.HelpRepository
import com.peter.azure.data.util.HELP_FILE_NAME
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.InputStream

class HelpRepositoryTest {

    private val assetManager = mockk<AssetManager>(relaxed = true)
    private val helpRepository = HelpRepository(assetManager)

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
    fun `get help map`() = runBlocking {
        val inputStream: InputStream = Json
            .encodeToString(helpList).byteInputStream()

        val resultError = helpRepository.getHelpMap()
        assertTrue(resultError is DataResult.Error)

        clearMocks(assetManager)

        every {
            assetManager.open(HELP_FILE_NAME)
        } returns inputStream

        helpRepository.getHelpMap()
        val resultSuccess = helpRepository.getHelpMap()

        coVerify(exactly = 1) {
            assetManager.open(HELP_FILE_NAME)
        }

        confirmVerified(assetManager)

        assertTrue(resultSuccess is DataResult.Success)
        val helpMap = resultSuccess as DataResult.Success
        assertEquals(expected.size, helpMap.result.size)
        assertEquals(expected, helpMap.result)
    }

}