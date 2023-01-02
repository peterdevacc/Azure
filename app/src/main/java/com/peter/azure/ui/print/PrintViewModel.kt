package com.peter.azure.ui.print

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.azure.data.entity.DataResult
import com.peter.azure.data.entity.GameLevel
import com.peter.azure.data.repository.PdfRepository
import com.peter.azure.data.repository.SudokuRepository
import com.peter.azure.data.util.PDF_PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrintViewModel @Inject constructor(
    private val sudokuRepository: SudokuRepository,
    private val pdfRepository: PdfRepository
) : ViewModel() {

    private val _pdfUiState: MutableState<PdfUiState> = mutableStateOf(PdfUiState.Default)
    val pdfUiState: State<PdfUiState> = _pdfUiState

    private val gameLevelListState = mutableStateOf(emptyList<GameLevel>())
    val gameLevelList: State<List<GameLevel>> = gameLevelListState

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

    fun generateSudokuPdf() {
        viewModelScope.launch {
            _pdfUiState.value = PdfUiState.Processing
            val boardList = sudokuRepository.getPrintGameList(gameLevelListState.value)
            when (val sudokuPdfResult = pdfRepository.generateSudokuPdf(boardList)) {
                is DataResult.Error -> {
                    _pdfUiState.value = PdfUiState.Error(sudokuPdfResult.code)
                }
                is DataResult.Success -> {
                    _pdfUiState.value = PdfUiState.Loaded(
                        sudokuPdfResult.result
                    )
                }
            }
        }
    }

    fun dismissErrorDialog() {
        _pdfUiState.value = PdfUiState.Default
    }

    override fun onCleared() {
        super.onCleared()
        pdfRepository.deleteCachePDF()
    }

}