package com.peter.azure.ui.help

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.azure.data.entity.DataResult
import com.peter.azure.data.repository.HelpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(
    private val helpRepository: HelpRepository
) : ViewModel() {

    private val helpUiState: MutableState<HelpUiState> = mutableStateOf(HelpUiState.Loading)
    val uiState: State<HelpUiState> = helpUiState

    init {
        viewModelScope.launch {
            when (val helpMapResult = helpRepository.getHelpMap()) {
                is DataResult.Error -> {
                    helpUiState.value = HelpUiState.Error(helpMapResult.code)
                }
                is DataResult.Success -> {
                    helpUiState.value = HelpUiState.Success(helpMapResult.result)
                }
            }
        }
    }

}