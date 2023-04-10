/*
 * Copyright (c) 2023 洪振健 All rights reserved.
 */

package com.peter.azure.ui.print

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.azure.data.entity.GameLevel
import com.peter.azure.data.repository.PdfRepository
import com.peter.azure.data.repository.SudokuRepository
import com.peter.azure.data.util.DataResult
import com.peter.azure.data.util.PDF_PAGE_SIZE
import com.peter.azure.util.azureSchedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PrintViewModel @Inject constructor(
    private val sudokuRepository: SudokuRepository,
    private val pdfRepository: PdfRepository
) : ViewModel() {

    private val _pdfUiState = MutableStateFlow<PdfUiState>(PdfUiState.Default)
    val pdfUiState = _pdfUiState.asStateFlow()

    private val gameLevelListState = MutableStateFlow<List<GameLevel>>(emptyList())
    val gameLevelList = gameLevelListState.asStateFlow()

    private var task: TimerTask? = null

    fun addGameLevel(gameLevel: GameLevel) {
        if (gameLevelListState.value.size < PDF_PAGE_SIZE) {
            val levelList = gameLevelListState.value.toMutableList()
            levelList.add(gameLevel)
            gameLevelListState.value = levelList
        }
    }

    fun removeGameLevel(gameLevel: GameLevel) {
        if (gameLevelListState.value.isNotEmpty()) {
            val levelList = gameLevelListState.value.toMutableList()
            levelList.remove(gameLevel)
            gameLevelListState.value = levelList
        }
    }

    fun generateSudokuPdf(
        appName: String, gameLevelTitlePrefix: String, gameLevelTextList: List<String>
    ) {
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            _pdfUiState.value = PdfUiState.Processing
            if (gameLevelListState.value.isNotEmpty()) {
                val boardList = sudokuRepository.getPrintGameList(gameLevelListState.value)
                val sudokuPdfResult = pdfRepository.generateSudokuPdf(
                    appName,
                    gameLevelTitlePrefix,
                    gameLevelTextList,
                    boardList
                )
                when (sudokuPdfResult) {
                    is DataResult.Error -> {
                        _pdfUiState.value = PdfUiState.Error(sudokuPdfResult.code)
                    }
                    is DataResult.Success -> {
                        _pdfUiState.value = PdfUiState.Loaded(sudokuPdfResult.result)
                    }
                }
            } else {
                _pdfUiState.value = PdfUiState.EmptyGameLevelList
            }
            task?.cancel()
        }
        task = scheduleLimit(job)
        job.start()
    }

    fun notShareable() {
        _pdfUiState.value = PdfUiState.NotShareable
    }

    fun dismissDialog() {
        _pdfUiState.value = PdfUiState.Default
    }

    override fun onCleared() {
        super.onCleared()
        pdfRepository.deleteCachePDF()
    }

    private fun scheduleLimit(job: Job) = azureSchedule {
        if (_pdfUiState.value is PdfUiState.Processing) {
            _pdfUiState.value = PdfUiState.Error(
                DataResult.Error.Code.UNKNOWN
            )
            job.cancel()
        }
    }

}