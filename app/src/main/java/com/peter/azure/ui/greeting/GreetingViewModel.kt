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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GreetingViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val infoRepository: InfoRepository
) : ViewModel() {

    private val greetingUiState: MutableState<GreetingUiState> =
        mutableStateOf(GreetingUiState.Default)
    val uiState: State<GreetingUiState> = greetingUiState

    fun loadInfo(infoType: Info.Type) {
        greetingUiState.value = GreetingUiState.Processing
        viewModelScope.launch {
            delay(600)
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
        }
    }

    fun dismissDialog() {
        greetingUiState.value = GreetingUiState.Default
    }

    fun agreeContracts() {
        viewModelScope.launch {
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
        }
    }

}