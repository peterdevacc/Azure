/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.data.util

import com.peter.common.Sudoku
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AzureSudokuModule {

    @Singleton
    @Provides
    fun provideAzureSudoku(): Sudoku {
        return Sudoku()
    }

}