package com.clean.architecture.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.clean.architecture.ui.theme.BabyBlue
import com.clean.architecture.ui.theme.LightGreen
import com.clean.architecture.ui.theme.RedOrange
import com.clean.architecture.ui.theme.RedPink
import com.clean.architecture.ui.theme.Violet

@Entity
data class Note(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
){

    companion object {
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}


