/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.data.repository

import android.content.res.AssetManager
import com.peter.azure.data.entity.Help
import com.peter.azure.data.util.DataResult
import com.peter.azure.data.util.HELP_FILE_NAME
import com.peter.azure.data.util.HELP_FILE_ZH_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HelpRepository @Inject constructor(
    private val assetManager: AssetManager,
) {

    private var currentLanguage = ""
    private var helpMap = emptyMap<Help.Catalog, List<Help>>()

    suspend fun getHelpMap(language: String): DataResult<Map<Help.Catalog, List<Help>>> =
        withContext(Dispatchers.IO) {
            if (currentLanguage != language) {
                try {
                    val fileName = if (language == "zh") {
                        HELP_FILE_ZH_NAME
                    } else {
                        HELP_FILE_NAME
                    }
                    val jsonFileInputStream = assetManager.open(fileName)
                    val jsonString = jsonFileInputStream.bufferedReader().readText()
                    val helpList = Json.decodeFromString<List<Help>>(jsonString)
                    helpMap = helpList.groupBy { it.catalog }
                    jsonFileInputStream.close()

                    currentLanguage = language
                } catch (exception: Exception) {
                    val code = if (exception is IOException) {
                        DataResult.Error.Code.IO
                    } else {
                        DataResult.Error.Code.UNKNOWN
                    }

                    return@withContext DataResult.Error(code)
                }
            }

            return@withContext DataResult.Success(helpMap)
        }

}