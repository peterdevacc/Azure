/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.greeting

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.azure.data.entity.Info
import com.peter.azure.data.repository.InfoRepository
import com.peter.azure.data.repository.PreferencesRepository
import com.peter.azure.data.util.DataResult
import com.peter.azure.util.azureSchedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class GreetingViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val infoRepository: InfoRepository
) : ViewModel() {

    private val greetingUiState: MutableState<GreetingUiState> =
        mutableStateOf(GreetingUiState.Default)
    val uiState: State<GreetingUiState> = greetingUiState

    private var task: TimerTask? = null

    fun loadInfo(infoType: Info.Type) {
        greetingUiState.value = GreetingUiState.Processing
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            when (val infoResult = infoRepository.getInfo(infoType)) {
                is DataResult.Error -> {
                    greetingUiState.value = GreetingUiState
                        .Error(infoResult.code)
                }
                is DataResult.Success -> {
                    greetingUiState.value = GreetingUiState
                        .InfoDialogLoaded(infoResult.result)
                }
            }
            task?.cancel()
        }
        task = scheduleLimit(job)
        job.start()
    }

    fun dismissDialog() {
        greetingUiState.value = GreetingUiState.Default
    }

    fun acceptContracts() {
        greetingUiState.value = GreetingUiState.Processing
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            when (val boardingResult = preferencesRepository.setOnBoardingState(true)) {
                is DataResult.Error -> {
                    greetingUiState.value = GreetingUiState
                        .Error(boardingResult.code)
                }
                is DataResult.Success -> {
                    greetingUiState.value = GreetingUiState
                        .InfoAccepted
                }
            }
            task?.cancel()
        }
        task = scheduleLimit(job)
        job.start()
    }

    private fun scheduleLimit(job: Job) = azureSchedule {
        if (greetingUiState.value is GreetingUiState.Processing) {
            greetingUiState.value = GreetingUiState.Error(
                DataResult.Error.Code.UNKNOWN
            )
            job.cancel()
        }
    }

}