package com.peter.azure.ui.contract

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.azure.data.entity.DataResult
import com.peter.azure.data.entity.Info
import com.peter.azure.data.repository.InfoRepository
import com.peter.azure.util.azureSchedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ContractViewModel @Inject constructor(
    private val infoRepository: InfoRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val contractUiState: MutableState<ContractUiState> =
        mutableStateOf(ContractUiState.Loading)
    val uiState: State<ContractUiState> = contractUiState

    private var task: TimerTask? = null

    private fun scheduleLimit(job: Job) = azureSchedule {
        if (contractUiState.value is ContractUiState.Loading) {
            contractUiState.value = ContractUiState.Error(
                DataResult.Error.Code.UNKNOWN
            )
            job.cancel()
        }
    }

    init {
        val infoType = savedStateHandle.get<String>("infoType") ?: ""
        if (infoType.isNotEmpty()) {
            val type = Info.Type.valueOf(infoType)
            val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
                when (val infoResult = infoRepository.getInfo(type)) {
                    is DataResult.Error -> {
                        contractUiState.value = ContractUiState
                            .Error(infoResult.code)
                    }
                    is DataResult.Success -> {
                        contractUiState.value = ContractUiState
                            .Success(infoResult.result)
                    }
                }
                task?.cancel()
            }
            task = scheduleLimit(job)
            job.start()
        } else {
            contractUiState.value = ContractUiState
                .Error(DataResult.Error.Code.UNKNOWN)
        }
    }

}