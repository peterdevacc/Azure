/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.azure.data.entity.GameLevel
import com.peter.azure.data.repository.PreferencesRepository
import com.peter.azure.data.util.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    preferencesRepository: PreferencesRepository,
) : ViewModel() {

    private val homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = homeUiState.asStateFlow()

    fun setDialAngle(angle: Double) {
        if (homeUiState.value is HomeUiState.Success) {
            val state = (homeUiState.value as HomeUiState.Success)
            homeUiState.value = state.copy(dialAngle = angle)
        }
    }

    fun getGameLevel(): GameLevel {
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

    init {
        val gameExistedFlow = preferencesRepository.getGameExistedState()
        viewModelScope.launch {
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