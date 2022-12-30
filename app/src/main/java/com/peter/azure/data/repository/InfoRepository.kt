package com.peter.azure.data.repository

import android.content.res.AssetManager
import com.peter.azure.data.entity.DataResult
import com.peter.azure.data.entity.Info
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InfoRepository @Inject constructor(
    private val assetManager: AssetManager,
) {

    private val infoMap = mutableMapOf<Info.Type, Info>()

    suspend fun getInfo(infoType: Info.Type): DataResult<Info> =
        withContext(Dispatchers.IO) {
            if (infoMap[infoType] == null) {
                try {
                    val jsonFileInputStream = assetManager.open(infoType.getFileName())
                    val jsonString = jsonFileInputStream.bufferedReader().readText()
                    val info = Json.decodeFromString<Info>(jsonString)
                    infoMap[infoType] = info
                    jsonFileInputStream.close()
                } catch (exception: Exception) {
                    val code = if (exception is IOException) {
                        DataResult.Error.Code.IO
                    } else {
                        DataResult.Error.Code.UNKNOWN
                    }

                    return@withContext DataResult.Error(code)
                }
            }

            return@withContext DataResult.Success(infoMap.getValue(infoType))
        }

}