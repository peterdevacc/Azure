package com.peter.azure.repository

import android.content.res.AssetManager
import com.peter.azure.data.util.DataResult
import com.peter.azure.data.entity.Info
import com.peter.azure.data.repository.InfoRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.InputStream

class InfoRepositoryTest {

    private val assetManager = mockk<AssetManager>(relaxed = true)
    private val infoRepository = InfoRepository(assetManager)

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
    fun `get info`() = runBlocking {
        val serviceInputStream: InputStream = Json
            .encodeToString(serviceInfo).byteInputStream()

        val resultError = infoRepository.getInfo(Info.Type.SERVICE)
        assertTrue(resultError is DataResult.Error)

        clearMocks(assetManager)

        every {
            assetManager.open(type.getFileName())
        } returns serviceInputStream

        infoRepository.getInfo(Info.Type.SERVICE)
        val resultSuccess = infoRepository.getInfo(Info.Type.SERVICE)

        coVerify(exactly = 1) {
            assetManager.open(type.getFileName())
        }

        confirmVerified(assetManager)

        assertTrue(resultSuccess is DataResult.Success)
        val info = resultSuccess as DataResult.Success
        assertEquals(serviceInfo, info.result)

    }

}