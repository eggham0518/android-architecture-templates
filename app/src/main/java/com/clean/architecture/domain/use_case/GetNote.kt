package com.clean.architecture.domain.use_case

import com.clean.architecture.data.repo.note.NoteRepository
import com.clean.architecture.data.source.local.entity.Note

class GetNote(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: String): Note? {
        return repository.getNoteById(id)
    }

}