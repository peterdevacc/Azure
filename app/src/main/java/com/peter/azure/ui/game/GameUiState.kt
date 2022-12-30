package com.peter.azure.ui.game

import com.peter.azure.data.entity.DataResult
import com.peter.azure.data.entity.Location
import com.peter.azure.data.entity.Mark
import com.peter.azure.data.entity.Puzzle

sealed interface GameUiState {

    data class Playing(
        val puzzle: Puzzle,
        val location: Location,
        val markList: List<Mark>,
        val dialog: Dialog,
        val isCorrect: Boolean,
    ): GameUiState {

        enum class Dialog {
            Submit, Delete, Processing, None
        }

    }

    object GameEnded: GameUiState

    object Loading: GameUiState

    data class Error(
        val code: DataResult.Error.Code
    ): GameUiState

}