package com.peter.azure.ui.greeting

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.azure.data.entity.DataResult
import com.peter.azure.data.entity.Info
import com.peter.azure.data.repository.InfoRepository
import com.peter.azure.data.repository.PreferencesRepository
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
                        .ContractDialogLoaded(infoResult.result)
                }
            }
            task?.cancel()
        }
        job.start()
        task = scheduleLimit(job)
    }

    fun dismissDialog() {
        greetingUiState.value = GreetingUiState.Default
    }

    fun agreeContracts() {
        greetingUiState.value = GreetingUiState.Processing
        val job = viewModelScope.launch {
            when (val boardingResult = preferencesRepository.setOnBoardingState(true)) {
                is DataResult.Error -> {
                    greetingUiState.value = GreetingUiState
                        .Error(boardingResult.code)
                }
                is DataResult.Success -> {
                    greetingUiState.value = GreetingUiState
                        .ContractsAgreed
                }
            }
            task?.cancel()
        }
        task = scheduleLimit(job)
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