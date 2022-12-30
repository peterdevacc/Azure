package com.peter.azure.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.azure.data.entity.DataResult
import com.peter.azure.data.repository.PreferencesRepository
import com.peter.azure.ui.navigation.AzureDestination
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

    private val mainUiState: MutableState<MainUiState> = mutableStateOf(MainUiState.Loading)
    val uiState: State<MainUiState> = mainUiState

    init {
        viewModelScope.launch {
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
        }
    }

}