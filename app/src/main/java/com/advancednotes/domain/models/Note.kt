package com.advancednotes.domain.models

import androidx.annotation.Keep
import com.advancednotes.common.Constants.Companion.EMPTY_ID
import com.advancednotes.common.Constants.Companion.EMPTY_UUID
import com.advancednotes.data.api.models.NoteModel
import com.advancednotes.data.database_notes.entities.NoteEntity
import com.advancednotes.utils.date.DateHelper.getCurrentDate
import com.advancednotes.utils.date.DateHelper.parseDateToString
import com.advancednotes.utils.type_adapters.AdvancedContentTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.Date

@Keep
data class Note(
    val id: Long = EMPTY_ID,
    val uuid: String = EMPTY_UUID,
    val name: String = "",
    val advancedContent: List<AdvancedContentItem>? = null,
    val tagsUUIDs: List<String> = emptyList(),
    val location: MyLocation? = null,
    val reminder: NoteReminder? = null,
    val fixed: Boolean = false,
    val archived: Boolean = false,
    val creationDate: Date = getCurrentDate(),
    val modificationDate: Date = getCurrentDate(),
    val trashDate: Date? = null,
    val deletedPermanently: Boolean = false
)

fun Note.toApi(): NoteModel {
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(AdvancedContentType::class.java, AdvancedContentTypeAdapter())
        .serializeNulls()
        .create()

    return NoteModel(
        id = id,
        uuid = uuid,
        name = name,
        advancedContent = if (advancedContent != null) {
            gson.toJson(advancedContent)
        } else null,
        tagsUUIDs = if (tagsUUIDs.isNotEmpty()) gson.toJson(tagsUUIDs) else null,
        location = if (location != null) gson.toJson(location) else null,
        reminder = parseNoteReminderToDatabase(reminder),
        fixed = fixed,
        archived = archived,
        creationDate = parseDateToString(creationDate),
        modificationDate = parseDateToString(modificationDate),
        trashDate = trashDate?.let { parseDateToString(it) },
        deletedPermanently = deletedPermanently
    )
}

fun Note.toDatabase(): NoteEntity {
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(AdvancedContentType::class.java, AdvancedContentTypeAdapter())
        .serializeNulls()
        .create()

    return NoteEntity(
        id = id,
        uuid = uuid,
        name = name,
        advancedContent = if (advancedContent != null) {
            gson.toJson(advancedContent)
        } else null,
        tagsUUIDs = if (tagsUUIDs.isNotEmpty()) gson.toJson(tagsUUIDs) else null,
        location = if (location != null) gson.toJson(location) else null,
        reminder = parseNoteReminderToDatabase(reminder),
        fixed = fixed,
        archived = archived,
        creationDate = parseDateToString(creationDate),
        modificationDate = parseDateToString(modificationDate),
        trashDate = trashDate?.let { parseDateToString(it) },
        deletedPermanently = deletedPermanently
    )
}

fun Note.isEmpty(): Boolean {
    return id == EMPTY_ID &&
            uuid == EMPTY_UUID &&
            name.isEmpty() &&
            advancedContent.isNullOrEmpty() &&
            tagsUUIDs.isEmpty() &&
            location == null &&
            reminder == null
}