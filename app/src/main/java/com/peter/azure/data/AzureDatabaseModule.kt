/*
 * Copyright 2022-2023 洪振健.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.peter.azure.data

import android.app.Application
import androidx.room.Room
import com.peter.azure.data.dao.NoteDAO
import com.peter.azure.data.dao.PuzzleDAO
import com.peter.azure.data.util.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AzureDatabaseModule {

    @Provides
    @Singleton
    fun provideAzureDatabase(application: Application): AzureDatabase {
        return Room.databaseBuilder(
            application,
            AzureDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePuzzleDAO(azureDatabase: AzureDatabase): PuzzleDAO {
        return azureDatabase.getPuzzleDAO()
    }

    @Provides
    @Singleton
    fun provideNoteDAO(azureDatabase: AzureDatabase): NoteDAO {
        return azureDatabase.getNoteDAO()
    }

}