package com.clean.architecture.domain.use_case

import com.clean.architecture.data.repo.note.NoteRepository
import com.clean.architecture.data.source.local.entity.Note
import com.clean.architecture.data.types.exeption.InvalidNoteException

class DeleteNote(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}