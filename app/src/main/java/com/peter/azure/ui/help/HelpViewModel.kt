/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.help

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.azure.data.repository.HelpRepository
import com.peter.azure.data.util.DataResult
import com.peter.azure.util.azureSchedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(
    private val helpRepository: HelpRepository
) : ViewModel() {

    private val helpUiState: MutableState<HelpUiState> = mutableStateOf(HelpUiState.Loading)
    val uiState: State<HelpUiState> = helpUiState

    private var task: TimerTask? = null

    private fun scheduleLimit(job: Job) = azureSchedule {
        if (helpUiState.value is HelpUiState.Loading) {
            helpUiState.value = HelpUiState.Error(
                DataResult.Error.Code.UNKNOWN
            )
            job.cancel()
        }
    }

    init {
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            when (val helpMapResult = helpRepository.getHelpMap()) {
                is DataResult.Error -> {
                    helpUiState.value = HelpUiState.Error(helpMapResult.code)
                }
                is DataResult.Success -> {
                    helpUiState.value = HelpUiState.Success(helpMapResult.result)
                }
            }
            task?.cancel()
        }
        task = scheduleLimit(job)
        job.start()
    }

}