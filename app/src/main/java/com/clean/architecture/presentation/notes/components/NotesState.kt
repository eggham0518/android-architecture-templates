package com.clean.architecture.presentation.notes.components

import com.clean.architecture.data.source.local.entity.Note
import com.clean.architecture.data.types.order.NoteOrder
import com.clean.architecture.data.types.order.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)