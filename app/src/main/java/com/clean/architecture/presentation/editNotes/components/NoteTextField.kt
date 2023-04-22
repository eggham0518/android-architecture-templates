package com.clean.architecture.presentation.editNotes.components

data class NoteTextField(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)