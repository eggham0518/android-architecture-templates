package com.clean.architecture.presentation.editNotes.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clean.architecture.data.source.local.entity.Note
import com.clean.architecture.data.types.exeption.InvalidNoteException
import com.clean.architecture.domain.use_case.NoteUseCases
import com.clean.architecture.presentation.editNotes.components.AddEditNoteEvent
import com.clean.architecture.presentation.editNotes.components.NoteTextField
import com.clean.architecture.presentation.editNotes.components.PostNoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _postNoteState = mutableStateOf(
        PostNoteState(
            noteTitleField = NoteTextField(
                hint = "Enter title..."
            ),
            noteContentField = NoteTextField(
                hint = "Enter some content"
            ),
            noteBackgroundColor = Color.DarkGray.toArgb(),
            uiEvent = UiEvent.Loading,
            onAadEditNoteEvent = {
                onEvent(it)
            }
        )
    )
    val postNoteState: State<PostNoteState> = _postNoteState

    private lateinit var currentNoteId: String

    init {
        savedStateHandle.get<String?>("noteId")?.let { noteId ->
            viewModelScope.launch {
                val selectedNote = noteUseCases.getNote(noteId)
                if (selectedNote == null) {
                    currentNoteId = UUID.randomUUID().toString()
                } else {
                    currentNoteId = selectedNote.id
                    _postNoteState.value = _postNoteState.value.copy(
                        noteTitleField = _postNoteState.value.noteTitleField.copy(
                            text = selectedNote.title,
                            isHintVisible = false
                        ),
                        noteContentField = _postNoteState.value.noteTitleField.copy(
                            text = selectedNote.content,
                            isHintVisible = false
                        ),
                        noteBackgroundColor = selectedNote.color
                    )
                }
            }
        }
    }

    private fun onEvent(event: AddEditNoteEvent) {
        with(_postNoteState.value) {
            when (event) {
                is AddEditNoteEvent.EnteredTitle -> {
                    copy(noteTitleField = noteTitleField.copy(text = event.value))
                }

                is AddEditNoteEvent.ChangeTitleFocus -> {
                    val isHintVisible = !event.focusState.isFocused && noteTitleField.text.isBlank()
                    copy(noteTitleField = noteTitleField.copy(isHintVisible = isHintVisible))
                }

                is AddEditNoteEvent.EnteredContent -> {
                    copy(noteContentField = noteContentField.copy(text = event.value))
                }

                is AddEditNoteEvent.ChangeContentFocus -> {
                    val isHintVisible =
                        !event.focusState.isFocused && noteContentField.text.isBlank()
                    copy(noteContentField = noteContentField.copy(isHintVisible = isHintVisible))
                }

                is AddEditNoteEvent.ChangeColor -> {
                    copy(noteBackgroundColor = event.color)
                }

                is AddEditNoteEvent.SaveNote -> {
                    saveNote()
                    return
                }
            }
        }.also { postNoteState ->
            if (event !is AddEditNoteEvent.SaveNote) _postNoteState.value = postNoteState
        }
    }

    private fun saveNote() {
        viewModelScope.launch {
            with(_postNoteState.value) {
                try {
                    val note = Note(
                        title = noteTitleField.text,
                        content = noteContentField.text,
                        timestamp = System.currentTimeMillis(),
                        color = noteBackgroundColor,
                        id = currentNoteId,
                    )
                    noteUseCases.addNote(note)
                    _postNoteState.value = copy(uiEvent = UiEvent.SaveNote)
                } catch (e: InvalidNoteException) {
                    val uiEvent = UiEvent.ShowSnackbar(
                        message = e.message ?: "Couldn't save note"
                    )
                    _postNoteState.value = copy(uiEvent = uiEvent)
                }
            }
        }
    }

    sealed class UiEvent {
        object Loading : UiEvent()
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}