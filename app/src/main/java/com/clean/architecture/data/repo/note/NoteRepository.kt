package com.clean.architecture.data.repo.note

import com.clean.architecture.data.di.module.IoDispatcher
import com.clean.architecture.data.source.local.database.dao.NoteDao
import com.clean.architecture.data.source.local.entity.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface NoteRepository {

    val notes: Flow<List<Note>>

    suspend fun getNoteById(id: String): Note?

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)

}

class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao,
   @IoDispatcher private val dispatcher: CoroutineDispatcher
) : NoteRepository {

    override val notes: Flow<List<Note>>
        get() = dao.getNotesFlow().flowOn(dispatcher)

    override suspend fun getNoteById(id: String): Note? = withContext(dispatcher) {
        dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) = withContext(dispatcher) {
        dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) = withContext(dispatcher) {
        dao.deleteNote(note)
    }

}