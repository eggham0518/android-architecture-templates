package com.clean.architecture.presentation.editNotes.components

import com.clean.architecture.presentation.editNotes.screen.AddEditNoteViewModel

data class PostNoteState(
    val noteTitleField: NoteTextField,
    val noteContentField: NoteTextField,
    val noteBackgroundColor: Int,
    val uiEvent: AddEditNoteViewModel.UiEvent,
    val onAadEditNoteEvent: (addEditNoteEvent: AddEditNoteEvent) -> Unit,
)