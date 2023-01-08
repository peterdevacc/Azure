package com.peter.azure.ui.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.azure.data.entity.DataResult
import com.peter.azure.data.entity.GameLevel
import com.peter.azure.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

    private val homeUiState: MutableState<HomeUiState> = mutableStateOf(HomeUiState.Loading)
    val uiState: State<HomeUiState> = homeUiState

    fun setGameLevel(angle: Double) {
        if (homeUiState.value is HomeUiState.Success) {
            val state = (homeUiState.value as HomeUiState.Success)
            homeUiState.value = state.copy(dialAngle = angle)
        }
    }

    fun getGameLevel(): GameLevel? {
        if (homeUiState.value is HomeUiState.Success) {
            val state = (homeUiState.value as HomeUiState.Success)
            val level = if (state.dialAngle >= -120.0 && state.dialAngle < 0.0) {
                GameLevel.HARD
            } else if (state.dialAngle in 0.0..120.0) {
                GameLevel.MODERATE
            } else {
                GameLevel.EASY
            }
            return level
        }
        return null
    }

    init {
        viewModelScope.launch {
            val gameExistedFlow = preferencesRepository.getGameExistedState()
            gameExistedFlow.collect { prefResult ->
                when (prefResult) {
                    is DataResult.Success -> {
                        homeUiState.value = HomeUiState.Success(
                            gameExisted = prefResult.result,
                            dialAngle = 0.0
                        )
                    }
                    is DataResult.Error -> {
                        homeUiState.value = HomeUiState.Error(prefResult.code)
                    }
                }
            }
        }
    }

}