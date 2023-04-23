package com.clean.architecture.presentation.editNotes.screen

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clean.architecture.data.di.module.DefaultDispatcher
import com.clean.architecture.data.source.local.entity.Note
import com.clean.architecture.data.types.exeption.InvalidNoteException
import com.clean.architecture.domain.use_case.NoteUseCases
import com.clean.architecture.presentation.editNotes.components.AddEditNoteEvent
import com.clean.architecture.presentation.editNotes.components.NoteTextField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    @DefaultDispatcher private val defaultCoroutineDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel(), AddEditNoteContract {

    private lateinit var currentNoteId: String

    private val _noteTitleField = MutableStateFlow(NoteTextField(hint = "Enter title..."))
    override val noteTitleField: StateFlow<NoteTextField> = _noteTitleField

    private val _noteContentField = MutableStateFlow(NoteTextField(hint = "Enter some content"))
    override val noteContentField: StateFlow<NoteTextField> = _noteContentField

    private val _noteBackgroundColor = MutableStateFlow(Color.DarkGray.toArgb())
    override val noteBackgroundColor: StateFlow<Int> = _noteBackgroundColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    override val eventFlow: SharedFlow<UiEvent> = _eventFlow

    init {
        initNote(savedStateHandle)
    }

    override fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitleField.value = _noteTitleField.value.copy(text = event.value)
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                val isHintVisible = !event.focusState.isFocused && _noteTitleField.value.text.isBlank()
                _noteTitleField.value = _noteTitleField.value.copy(isHintVisible = isHintVisible)
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContentField.value = _noteContentField.value.copy(text = event.value)
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                val isHintVisible =
                        !event.focusState.isFocused && _noteContentField.value.text.isBlank()
                _noteContentField.value = _noteContentField.value.copy(isHintVisible = isHintVisible)
            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteBackgroundColor.value = event.color
            }

            AddEditNoteEvent.SaveNote -> {
                saveNote()
            }
        }
    }

    private fun initNote(savedStateHandle: SavedStateHandle) {
        viewModelScope.launch {
            val noteId = savedStateHandle.get<String?>("noteId")
            if (noteId == null) {
                initNewNoteId()
                return@launch
            }
            val selectedNote = noteUseCases.getNote(noteId)
            if (selectedNote == null) {
                initNewNoteId()
            } else {
                initSelectedNote(selectedNote)
            }
        }
    }

    private suspend fun initNewNoteId() {
        currentNoteId = withContext(defaultCoroutineDispatcher) { UUID.randomUUID().toString() }
    }

    private fun initSelectedNote(selectedNote: Note) {
        currentNoteId = selectedNote.id
        _noteTitleField.value = _noteTitleField.value.copy(
            text = selectedNote.title,
            isHintVisible = false
        )
        _noteContentField.value = _noteContentField.value.copy(
            text = selectedNote.content,
            isHintVisible = false
        )
        _noteBackgroundColor.value = selectedNote.color
    }

    private fun saveNote() {
        viewModelScope.launch {
                try {
                    val note = Note(
                        title = noteTitleField.value.text,
                        content = noteContentField.value.text,
                        timestamp = System.currentTimeMillis(),
                        color = noteBackgroundColor.value,
                        id = currentNoteId,
                    )
                    noteUseCases.addNote(note)
                    _eventFlow.emit(UiEvent.SaveNote)
                } catch (e: InvalidNoteException) {
                    val uiEvent = UiEvent.ShowSnackbar(
                        message = e.message ?: "Couldn't save note"
                    )
                    _eventFlow.emit(uiEvent)
                }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }

}

interface AddEditNoteContract {
    val noteTitleField: StateFlow<NoteTextField>
    val noteContentField: StateFlow<NoteTextField>
    val noteBackgroundColor: StateFlow<Int>
    val eventFlow: SharedFlow<AddEditNoteViewModel.UiEvent>

    fun onEvent(event: AddEditNoteEvent)
}

