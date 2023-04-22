package com.clean.architecture.domain.use_case

import com.clean.architecture.data.repo.note.NoteRepository
import com.clean.architecture.data.source.local.entity.Note
import com.clean.architecture.data.types.exeption.InvalidNoteException

class AddNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        if(note.title.isBlank()) {
            throw InvalidNoteException("The title of the note can't be empty.")
        }
        if(note.content.isBlank()) {
            throw InvalidNoteException("The content of the note can't be empty.")
        }
        repository.insertNote(note)
    }
}