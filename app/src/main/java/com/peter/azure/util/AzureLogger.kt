package com.peter.azure.util

import android.util.Log

const val DEBUGGER_LOG_TAG = "azure-sudoku-log"

fun azureLog(text: String) {
    Log.d(DEBUGGER_LOG_TAG, text)
}
