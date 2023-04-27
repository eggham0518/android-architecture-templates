package com.clean.architecture.data.di.module

import com.clean.architecture.data.repo.note.NoteRepository
import com.clean.architecture.data.repo.note.NoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun providesNoteRepository(noteRepositoryImpl: NoteRepositoryImpl): NoteRepository

}