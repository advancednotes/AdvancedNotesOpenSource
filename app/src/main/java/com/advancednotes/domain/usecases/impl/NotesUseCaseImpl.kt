package com.advancednotes.domain.usecases.impl

import android.content.Context
import com.advancednotes.common.Constants.Companion.EMPTY_ID
import com.advancednotes.common.Constants.Companion.EMPTY_UUID
import com.advancednotes.common.Constants.Companion.WITHOUT_TAG_UUID
import com.advancednotes.data.database_notes.entities.NoteEntity
import com.advancednotes.data.database_notes.entities.toDomain
import com.advancednotes.data.database_notes.repositories.NotesRepository
import com.advancednotes.domain.models.AdvancedContentItem
import com.advancednotes.domain.models.MyLocation
import com.advancednotes.domain.models.Note
import com.advancednotes.domain.models.NoteOnlyReminders
import com.advancednotes.domain.models.NoteOnlyTagsUUIDs
import com.advancednotes.domain.models.NoteOnlyTrashDate
import com.advancednotes.domain.models.NoteReminder
import com.advancednotes.domain.models.parseNoteReminderToDatabase
import com.advancednotes.domain.models.toDatabase
import com.advancednotes.domain.usecases.NotesFilesUseCase
import com.advancednotes.domain.usecases.NotesUseCase
import com.advancednotes.ui.screens.main.MainActivityViewModel.Companion.setNotesCount
import com.advancednotes.utils.date.DateHelper.getCurrentDate
import com.advancednotes.utils.date.DateHelper.parseDateToString
import com.advancednotes.utils.date.DateHelper.parseStringToDate
import com.advancednotes.utils.notifications.StickyNoteNotificationHelper
import com.advancednotes.utils.uuid.UUIDHelper.getRandomUUID
import com.google.gson.Gson
import java.util.Date
import javax.inject.Inject

class NotesUseCaseImpl @Inject constructor(
    private val notesRepository: NotesRepository,
    private val notesFilesUseCase: NotesFilesUseCase,
    private val stickyNoteNotificationHelper: StickyNoteNotificationHelper,
    private val gson: Gson
) : NotesUseCase {

    override fun duplicateNote(context: Context, noteAux: Note) {
        val newNoteAux: Note = noteAux.copy(
            id = EMPTY_ID,
            uuid = getRandomUUID(),
            creationDate = getCurrentDate(),
            modificationDate = getCurrentDate()
        )

        val oldNoteUUID: String = noteAux.uuid
        val newNoteId: Long = notesRepository.insertNote(newNoteAux.toDatabase())
        val newNoteUUID: String = notesRepository.getNoteUUIDById(newNoteId)

        notesFilesUseCase.duplicateNoteFiles(
            oldNoteUUID = oldNoteUUID,
            newNoteUUID = newNoteUUID
        )

        refreshNotesCountState()
    }

    override fun getAllNotes(): List<Note> {
        return notesRepository.getAllNotes().map { it.toDomain() }
    }

    override fun getNotes(
        filterQuery: String,
        filterNoteTagsUUIDs: List<String>
    ): List<Note> {
        val filterUncategorizedTags = filterNoteTagsUUIDs.any { it == WITHOUT_TAG_UUID }

        val notes: MutableList<Note> =
            notesRepository.getNotes().map { it.toDomain() } as MutableList<Note>

        if (filterQuery.isNotEmpty()) {
            notes.retainAll { note ->
                note.name.contains(filterQuery)
            }
        }

        if (filterNoteTagsUUIDs.isNotEmpty()) {
            notes.retainAll { note ->
                val isUncategorizedNote = filterUncategorizedTags && note.tagsUUIDs.isEmpty()

                isUncategorizedNote || note.tagsUUIDs.any { it in filterNoteTagsUUIDs }
            }
        }

        val sortedNotes: List<Note> = notes.sortedWith(
            compareByDescending<Note> { it.fixed }
                .thenByDescending { it.modificationDate }
        )

        return sortedNotes
    }

    override fun getNotesArchived(
        filterQuery: String,
        filterNoteTagsUUIDs: List<String>
    ): List<Note> {
        val notes: MutableList<Note> =
            notesRepository.getNotesArchived().map { it.toDomain() } as MutableList<Note>

        if (filterQuery.isNotEmpty()) {
            notes.retainAll { note ->
                note.name.contains(filterQuery)
            }
        }

        if (filterNoteTagsUUIDs.isNotEmpty()) {
            notes.retainAll { note ->
                note.tagsUUIDs.any { it in filterNoteTagsUUIDs }
            }
        }

        val sortedNotes: List<Note> = notes.sortedWith(
            compareByDescending<Note> { it.fixed }
                .thenByDescending { it.modificationDate }
        )

        return sortedNotes
    }

    override fun getNotesTrashed(
        filterQuery: String,
        filterNoteTagsUUIDs: List<String>
    ): List<Note> {
        val notes: MutableList<Note> =
            notesRepository.getNotesTrashed().map { it.toDomain() } as MutableList<Note>

        if (filterQuery.isNotEmpty()) {
            notes.retainAll { note ->
                note.name.contains(filterQuery)
            }
        }

        if (filterNoteTagsUUIDs.isNotEmpty()) {
            notes.retainAll { note ->
                note.tagsUUIDs.any { it in filterNoteTagsUUIDs }
            }
        }

        val sortedNotes: List<Note> = notes.sortedWith(
            compareByDescending<Note> { it.fixed }
                .thenByDescending { it.modificationDate }
        )

        return sortedNotes
    }

    override fun getNoteByUUID(uuid: String): Note {
        val note: Note? = notesRepository.getNoteByUUID(uuid)?.toDomain()

        return note ?: Note()
    }

    override fun getNoteUUIDById(id: Long): String {
        return notesRepository.getNoteUUIDById(id)
    }

    override fun getNotesTags(): List<NoteOnlyTagsUUIDs> {
        return notesRepository.getNotesTags().map { it.toDomain() }
    }

    override fun getNotesReminders(): List<NoteOnlyReminders> {
        return notesRepository.getNotesReminders().map { it.toDomain() }
    }

    override fun getNotesTrashDates(): List<NoteOnlyTrashDate> {
        return notesRepository.getNotesTrashDates().map { it.toDomain() }
    }

    override fun getNotesModificationDates(): List<Date> {
        return notesRepository.getNotesModificationDates().map { parseStringToDate(it) }
    }

    override fun insertNote(note: Note): Long {
        val noteAux: Note = if (note.uuid == EMPTY_UUID) {
            note.copy(
                uuid = getRandomUUID()
            )
        } else note

        val noteId: Long = notesRepository.insertNote(
            noteEntity = noteAux.toDatabase()
        )

        refreshNotesCountState()

        return noteId
    }

    override fun updateNote(note: Note, fromSynchronize: Boolean) {
        val noteAux: Note = if (!fromSynchronize) {
            note.copy(
                id = note.id,
                modificationDate = getCurrentDate()
            )
        } else note

        notesRepository.updateNote(
            noteEntity = noteAux.toDatabase()
        )
    }

    override fun updateNoteName(uuid: String, name: String) {
        notesRepository.updateNoteName(
            uuid = uuid,
            name = name,
            modificationDate = parseDateToString(getCurrentDate())
        )
    }

    override fun updateNoteAdvancedContent(
        uuid: String,
        advancedContent: List<AdvancedContentItem>?
    ) {
        notesRepository.updateNoteContent(
            uuid = uuid,
            advancedContent = gson.toJson(advancedContent),
            modificationDate = parseDateToString(getCurrentDate())
        )
    }

    override fun updateNoteTagsUUIDs(uuid: String, tagsUUIDs: List<String>) {
        notesRepository.updateNoteTagsUUIDs(
            uuid = uuid,
            tagsUUIDs = if (tagsUUIDs.isNotEmpty()) gson.toJson(tagsUUIDs) else null,
            modificationDate = parseDateToString(getCurrentDate())
        )
    }

    override fun updateNoteLocation(uuid: String, location: MyLocation?) {
        notesRepository.updateNoteLocation(
            uuid = uuid,
            location = if (location != null) gson.toJson(location) else null,
            modificationDate = parseDateToString(getCurrentDate())
        )
    }

    override fun updateNoteReminder(uuid: String, reminder: NoteReminder?) {
        notesRepository.updateReminder(
            uuid = uuid,
            reminder = parseNoteReminderToDatabase(reminder),
            modificationDate = parseDateToString(getCurrentDate())
        )
    }

    override fun updateNoteFixed(uuid: String, fixed: Boolean) {
        notesRepository.updateNoteFixed(
            uuid = uuid,
            fixed = fixed,
            modificationDate = parseDateToString(getCurrentDate())
        )
    }

    override fun updateNoteArchived(uuid: String, archived: Boolean) {
        notesRepository.updateNoteArchived(
            uuid = uuid,
            archived = archived,
            modificationDate = parseDateToString(getCurrentDate())
        )

        refreshNotesCountState()
    }

    override fun updateNoteTrash(uuid: String, trash: Boolean) {
        notesRepository.updateNoteTrashDate(
            uuid = uuid,
            trashDate = if (trash) parseDateToString(getCurrentDate()) else null,
            modificationDate = parseDateToString(getCurrentDate())
        )

        stickyNoteNotificationHelper.dismissStickyNoteNotificationIfExistsByNoteUUID(uuid)

        refreshNotesCountState()
    }

    override fun updateNoteDeletedPermanently(uuid: String) {
        notesRepository.updateNoteDeletedPermanently(
            uuid = uuid,
            modificationDate = parseDateToString(getCurrentDate())
        )

        notesFilesUseCase.updateAllNoteFilesByNoteUUIDDeletedPermanently(uuid)

        refreshNotesCountState()
    }

    override fun deleteAllNotes() {
        notesRepository.deleteAllNotes()
        notesFilesUseCase.deleteAllNotesFiles()
    }

    override fun deleteNotesTrashed() {
        val notesTrashed: List<NoteEntity> = notesRepository.getNotesTrashed()

        notesTrashed.forEach {
            updateNoteDeletedPermanently(it.uuid)
        }

        refreshNotesCountState()
    }

    override fun deleteNotesDeletedPermanently() {
        notesRepository.deleteNotesDeletedPermanently()
    }

    override fun refreshNotesCountState() {
        val countNotes = notesRepository.getCountNotes()
        val countArchivedNotes = notesRepository.getCountArchivedNotes()
        val countTrashedNotes = notesRepository.getCountTrashedNotes()

        setNotesCount(
            countNotes = countNotes,
            countArchivedNotes = countArchivedNotes,
            countTrashedNotes = countTrashedNotes
        )
    }
}