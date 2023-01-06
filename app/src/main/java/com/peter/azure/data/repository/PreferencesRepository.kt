package com.peter.azure.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.peter.azure.data.entity.DataResult
import com.peter.azure.data.util.GAME_EXISTED_PREF_KEY
import com.peter.azure.data.util.ON_BOARDING_PREF_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private val onBoardingKey = booleanPreferencesKey(ON_BOARDING_PREF_KEY)
    private val gameExistedKey = booleanPreferencesKey(GAME_EXISTED_PREF_KEY)

    suspend fun getOnBoardingState(): DataResult<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val pref = dataStore.data.first()

                return@withContext DataResult.Success(pref[onBoardingKey] ?: false)
            } catch (exception: Exception) {
                return@withContext DataResult.Error(DataResult.Error.Code.UNKNOWN)
            }
        }

    suspend fun setOnBoardingState(value: Boolean): DataResult<String> =
        withContext(Dispatchers.IO) {
            try {
                dataStore.edit { pref ->
                    pref[onBoardingKey] = value
                }

                return@withContext DataResult.Success("")
            } catch (exception: Exception) {
                val code = if (exception is IOException) {
                    DataResult.Error.Code.IO
                } else {
                    DataResult.Error.Code.UNKNOWN
                }

                return@withContext DataResult.Error(code)
            }
        }

    suspend fun getGameExistedState(): Flow<DataResult<Boolean>> {
        return dataStore.data
            .catch {
                emit(emptyPreferences())
            }
            .map { pref ->
                if (pref.asMap().isEmpty()) {
                    DataResult.Error(DataResult.Error.Code.IO)
                } else {
                    DataResult.Success(pref[gameExistedKey] ?: false)
                }
            }

    }

    suspend fun setGameExistedState(value: Boolean): DataResult<String> =
        withContext(Dispatchers.IO) {
            try {
                dataStore.edit { pref ->
                    pref[gameExistedKey] = value
                }

                return@withContext DataResult.Success("")
            } catch (exception: Exception) {
                val code = if (exception is IOException) {
                    DataResult.Error.Code.IO
                } else {
                    DataResult.Error.Code.UNKNOWN
                }

                return@withContext DataResult.Error(code)
            }
        }

}