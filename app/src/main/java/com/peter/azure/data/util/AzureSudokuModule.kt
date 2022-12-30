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