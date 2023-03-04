/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.azure.data.repository.PreferencesRepository
import com.peter.azure.data.util.DataResult
import com.peter.azure.ui.navigation.AzureDestination
import com.peter.azure.util.azureSchedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

    private val mainUiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState = mainUiState.asStateFlow()

    private var task: TimerTask? = null

    private fun scheduleLimit(job: Job) = azureSchedule {
        if (mainUiState.value is MainUiState.Loading) {
            mainUiState.value = MainUiState.Error(
                DataResult.Error.Code.UNKNOWN
            )
            job.cancel()
        }
    }

    init {
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            when (val boardingResult = preferencesRepository.getOnBoardingState()) {
                is DataResult.Success<Boolean> -> {
                    if (boardingResult.result) {
                        mainUiState.value = MainUiState
                            .Success(startDestination = AzureDestination.Main.HOME.route)
                    } else {
                        mainUiState.value = MainUiState
                            .Success(startDestination = AzureDestination.General.GREETING.route)
                    }
                }
                is DataResult.Error -> {
                    mainUiState.value = MainUiState
                        .Error(code = boardingResult.code)
                }
            }
            task?.cancel()
        }
        task = scheduleLimit(job)
        job.start()
    }

}