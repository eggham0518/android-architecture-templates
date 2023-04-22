package com.clean.architecture.presentation.notes.components

import com.clean.architecture.data.source.local.entity.Note
import com.clean.architecture.data.types.order.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder): NotesEvent()
    data class DeleteNote(val note: Note): NotesEvent()
    object RestoreNote: NotesEvent()
    object ToggleOrderSection: NotesEvent()
}