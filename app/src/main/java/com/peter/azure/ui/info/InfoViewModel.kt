/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.ui.info

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.azure.data.util.DataResult
import com.peter.azure.data.entity.Info
import com.peter.azure.data.repository.InfoRepository
import com.peter.azure.data.util.INFO_TYPE_SAVED_KEY
import com.peter.azure.util.azureSchedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val infoRepository: InfoRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val infoUiState: MutableState<InfoUiState> = mutableStateOf(InfoUiState.Loading)
    val uiState: State<InfoUiState> = infoUiState

    private var task: TimerTask? = null

    private fun scheduleLimit(job: Job) = azureSchedule {
        if (infoUiState.value is InfoUiState.Loading) {
            infoUiState.value = InfoUiState.Error(
                DataResult.Error.Code.UNKNOWN
            )
            job.cancel()
        }
    }

    init {
        val infoType: String = checkNotNull(savedStateHandle[INFO_TYPE_SAVED_KEY])
        val type = Info.Type.valueOf(infoType)
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            when (val infoResult = infoRepository.getInfo(type)) {
                is DataResult.Error -> {
                    infoUiState.value = InfoUiState
                        .Error(infoResult.code)
                }
                is DataResult.Success -> {
                    infoUiState.value = InfoUiState
                        .Success(infoResult.result)
                }
            }
            task?.cancel()
        }
        task = scheduleLimit(job)
        job.start()
    }

}