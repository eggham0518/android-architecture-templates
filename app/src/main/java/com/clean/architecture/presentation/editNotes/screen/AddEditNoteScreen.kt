package com.clean.architecture.presentation.editNotes.screen

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clean.architecture.data.source.local.entity.Note
import com.clean.architecture.presentation.editNotes.components.AddEditNoteEvent
import com.clean.architecture.presentation.editNotes.components.NoteTextField
import com.clean.architecture.presentation.editNotes.components.PostNoteState
import com.clean.architecture.presentation.editNotes.components.TransparentHintTextField
import com.clean.architecture.ui.theme.ArchitectureTemplatesTheme
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun AddEditNoteScreenPreview() {
    ArchitectureTemplatesTheme {
        AddEditNoteScreen(
            PostNoteState(
                noteTitleField = NoteTextField(
                    hint = "Enter title..."
                ),
                noteContentField = NoteTextField(
                    hint = "Enter some content"
                ),
                noteBackgroundColor = Color.White.toArgb(),
                uiEvent = AddEditNoteViewModel.UiEvent.Loading,
                onAadEditNoteEvent = {

                }
            ),
            noteColor = -1,
            onNavigateUp = {

            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    postNoteState: PostNoteState,
    noteColor: Int,
    onNavigateUp: () -> Unit,
) {

    val titleState = postNoteState.noteTitleField
    val contentState = postNoteState.noteContentField

    val noteBackgroundAnimaitable = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else postNoteState.noteBackgroundColor)
        )
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = postNoteState.uiEvent, block = {
        when (postNoteState.uiEvent) {
            is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(
                    message = postNoteState.uiEvent.message
                )
            }

            is AddEditNoteViewModel.UiEvent.SaveNote -> {
                onNavigateUp()
            }

            AddEditNoteViewModel.UiEvent.Loading -> {

            }
        }
    })

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    postNoteState.onAadEditNoteEvent(AddEditNoteEvent.SaveNote)
                },
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save note")
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimaitable.value)
                .padding(it)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Note.noteColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (postNoteState.noteBackgroundColor == colorInt) {
                                    Color.Black
                                } else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteBackgroundAnimaitable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                postNoteState.onAadEditNoteEvent(AddEditNoteEvent.ChangeColor(colorInt))
                            }
                    )
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    postNoteState.onAadEditNoteEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    postNoteState.onAadEditNoteEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    postNoteState.onAadEditNoteEvent(AddEditNoteEvent.EnteredContent(it))
                },
                onFocusChange = {
                    postNoteState.onAadEditNoteEvent(AddEditNoteEvent.ChangeContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

