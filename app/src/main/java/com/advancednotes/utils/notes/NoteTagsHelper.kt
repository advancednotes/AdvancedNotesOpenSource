package com.advancednotes.utils.notes

import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import com.advancednotes.common.Constants.Companion.WITHOUT_TAG_UUID
import com.advancednotes.domain.models.Note
import com.advancednotes.domain.models.Tag
import com.advancednotes.ui.theme.Blue
import com.advancednotes.ui.theme.Brown
import com.advancednotes.ui.theme.Gray
import com.advancednotes.ui.theme.Green
import com.advancednotes.ui.theme.Orange
import com.advancednotes.ui.theme.Pink
import com.advancednotes.ui.theme.Purple
import com.advancednotes.ui.theme.Red
import com.advancednotes.ui.theme.Turquoise
import com.advancednotes.ui.theme.Yellow
import com.advancednotes.utils.colors.ColorsHelper.getHexColor

object NoteTagsHelper {

    fun getNoteTagsDefaultColors(): List<String> {
        return listOf(
            getHexColor(Red),
            getHexColor(Orange),
            getHexColor(Yellow),
            getHexColor(Green),
            getHexColor(Turquoise),
            getHexColor(Blue),
            getHexColor(Purple),
            getHexColor(Pink),
            getHexColor(Brown),
            getHexColor(Gray),
            getHexColor(White),
            getHexColor(Black),
        )
    }

    fun getUsedNoteTags(
        notes: List<Note>,
        tags: List<Tag>,
        selectedTagsUUIDs: List<String>
    ): List<Tag> {
        val usedNoteTagUUIDS: MutableList<String> =
            (notes.flatMap { it.tagsUUIDs } + selectedTagsUUIDs).distinct().toMutableList()

        if (usedNoteTagUUIDS.size > 1 && notes.any { it.tagsUUIDs.isEmpty() }) {
            usedNoteTagUUIDS.add(WITHOUT_TAG_UUID)
        }

        val distinctSortedUsedTagsUUIDsByCount: List<String> = usedNoteTagUUIDS
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .map { it.key }

        return tags.filter { it.uuid in distinctSortedUsedTagsUUIDsByCount }
    }
}