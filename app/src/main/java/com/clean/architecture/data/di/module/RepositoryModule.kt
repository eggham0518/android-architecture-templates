package com.clean.architecture.data.di.module

import com.clean.architecture.data.repo.note.NoteRepository
import com.clean.architecture.data.repo.note.NoteRepositoryImpl
import com.clean.architecture.data.source.local.database.dao.NoteDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesNoteRepository(
        noteDao: NoteDao,
       @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): NoteRepository {
        return NoteRepositoryImpl(noteDao,coroutineDispatcher)
    }

}