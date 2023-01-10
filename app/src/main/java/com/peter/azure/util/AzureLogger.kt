/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.util

import android.util.Log

const val DEBUGGER_LOG_TAG = "azure-sudoku-log"

fun azureLog(text: String) {
    Log.d(DEBUGGER_LOG_TAG, text)
}
