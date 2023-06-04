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
    private val gameLevelFlow = MutableStateFlow(GameLevel.EASY)
    private val gameLevelValueList = GameLevel.values()

    val homeUiState = gameExistedFlow
        .combine(gameLevelFlow) { prefResult, gameLevel ->
            prefResult to gameLevel
        }.map { (prefResult, gameLevel) ->
            when (prefResult) {
                is DataResult.Error -> {
                    HomeUiState.Error(prefResult.code)
                }

                is DataResult.Success -> {
                    HomeUiState.Success(
                        gameExisted = prefResult.result,
                        gameLevel = gameLevel
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3_000, 0),
            initialValue = HomeUiState.Loading,
        )

    fun getGameLevel(): GameLevel {
        val state = (homeUiState.value as HomeUiState.Success)
        return state.gameLevel
    }

    fun setGameLevel(gameLevelNum: Int) {
        if (homeUiState.value is HomeUiState.Success) {
            gameLevelFlow.value = gameLevelValueList[gameLevelNum]
        }
    }

}