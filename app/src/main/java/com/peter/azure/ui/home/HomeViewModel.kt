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

    fun getGameLevel(angle: Double) {
        if (homeUiState.value is HomeUiState.Success) {
            val state = (homeUiState.value as HomeUiState.Success)
            val level = if (angle >= -120.0 && angle < 0.0) {
                GameLevel.HARD
            } else if (angle in 0.0..120.0) {
                GameLevel.MODERATE
            } else {
                GameLevel.EASY
            }
            homeUiState.value = state.copy(gameLevel = level)
        }
    }

    init {
        viewModelScope.launch {
            val gameExistedFlow = preferencesRepository.getGameExistedState()
            gameExistedFlow.collect { prefResult ->
                when (prefResult) {
                    is DataResult.Success -> {
                        if (homeUiState.value is HomeUiState.Success) {
                            homeUiState.value = (homeUiState.value as HomeUiState.Success)
                                .copy(gameExisted = prefResult.result)
                        } else {
                            homeUiState.value = HomeUiState.Success(
                                gameExisted = prefResult.result,
                                gameLevel = GameLevel.EASY
                            )
                        }
                    }
                    is DataResult.Error -> {
                        homeUiState.value = HomeUiState.Error(prefResult.code)
                    }
                }
            }
        }
    }

}