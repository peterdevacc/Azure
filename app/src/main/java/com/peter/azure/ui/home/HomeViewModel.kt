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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    preferencesRepository: PreferencesRepository,
) : ViewModel() {

    private val gameExistedFlow = preferencesRepository.getGameExistedState()
    private val dialAngleFlow = MutableStateFlow(0.0)

    val homeUiState = gameExistedFlow
        .combine(dialAngleFlow) { prefResult, dialAngle ->
            prefResult to dialAngle
        }.map { (prefResult, angle) ->
            when (prefResult) {
                is DataResult.Error -> {
                    HomeUiState.Error(prefResult.code)
                }
                is DataResult.Success -> {
                    HomeUiState.Success(
                        gameExisted = prefResult.result,
                        dialAngle = angle
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3_000, 0),
            initialValue = HomeUiState.Loading,
        )

    fun setDialAngle(angle: Double) {
        if (homeUiState.value is HomeUiState.Success) {
            dialAngleFlow.value = angle
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

}