package com.clean.architecture.domain.di

import com.clean.architecture.data.repo.note.NoteRepository
import com.clean.architecture.domain.use_case.AddNote
import com.clean.architecture.domain.use_case.DeleteNote
import com.clean.architecture.domain.use_case.GetNote
import com.clean.architecture.domain.use_case.GetNotes
import com.clean.architecture.domain.use_case.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun providesNoteUseCases(
        repository: NoteRepository,
    ): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository, Dispatchers.Default),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository),
        )
    }

}