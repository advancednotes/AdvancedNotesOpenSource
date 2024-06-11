package com.advancednotes.domain.usecases

import android.content.Context
import com.advancednotes.domain.models.AdvancedContentItem
import com.advancednotes.domain.models.MyLocation
import com.advancednotes.domain.models.Note
import com.advancednotes.domain.models.NoteOnlyReminders
import com.advancednotes.domain.models.NoteOnlyTagsUUIDs
import com.advancednotes.domain.models.NoteOnlyTrashDate
import com.advancednotes.domain.models.NoteReminder
import java.util.Date

interface NotesUseCase {

    fun duplicateNote(context: Context, noteAux: Note)

    fun getAllNotes(): List<Note>
    fun getNotes(
        filterQuery: String = "",
        filterNoteTagsUUIDs: List<String> = emptyList()
    ): List<Note>

    fun getNotesArchived(
        filterQuery: String = "",
        filterNoteTagsUUIDs: List<String> = emptyList()
    ): List<Note>

    fun getNotesTrashed(
        filterQuery: String = "",
        filterNoteTagsUUIDs: List<String> = emptyList()
    ): List<Note>

    fun getNoteByUUID(uuid: String): Note
    fun getNoteUUIDById(id: Long): String

    fun getNotesTags(): List<NoteOnlyTagsUUIDs>
    fun getNotesReminders(): List<NoteOnlyReminders>
    fun getNotesTrashDates(): List<NoteOnlyTrashDate>
    fun getNotesModificationDates(): List<Date>

    fun insertNote(note: Note): Long

    fun updateNote(note: Note, fromSynchronize: Boolean = false)
    fun updateNoteName(uuid: String, name: String)
    fun updateNoteAdvancedContent(uuid: String, advancedContent: List<AdvancedContentItem>?)
    fun updateNoteTagsUUIDs(uuid: String, tagsUUIDs: List<String>)
    fun updateNoteLocation(uuid: String, location: MyLocation?)
    fun updateNoteReminder(uuid: String, reminder: NoteReminder?)
    fun updateNoteFixed(uuid: String, fixed: Boolean)
    fun updateNoteArchived(uuid: String, archived: Boolean)
    fun updateNoteTrash(uuid: String, trash: Boolean)
    fun updateNoteDeletedPermanently(uuid: String)

    fun deleteAllNotes()
    fun deleteNotesTrashed()
    fun deleteNotesDeletedPermanently()

    fun refreshNotesCountState()
}