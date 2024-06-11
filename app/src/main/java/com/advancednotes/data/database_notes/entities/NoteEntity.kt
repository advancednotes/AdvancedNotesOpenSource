package com.advancednotes.data.database_notes.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.advancednotes.domain.models.AdvancedContentItem
import com.advancednotes.domain.models.AdvancedContentType
import com.advancednotes.domain.models.MyLocation
import com.advancednotes.domain.models.Note
import com.advancednotes.domain.models.parseNoteReminderToDomain
import com.advancednotes.utils.date.DateHelper.parseStringToDate
import com.advancednotes.utils.type_adapters.AdvancedContentTypeAdapter
import com.google.gson.GsonBuilder

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "uuid")
    val uuid: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "advanced_content")
    val advancedContent: String?,

    @ColumnInfo(name = "tags_uuids")
    val tagsUUIDs: String?,

    @ColumnInfo(name = "location")
    val location: String?,

    @ColumnInfo(name = "reminder")
    val reminder: String?,

    @ColumnInfo(name = "fixed")
    val fixed: Boolean,

    @ColumnInfo(name = "archived")
    val archived: Boolean,

    @ColumnInfo(name = "creation_date")
    val creationDate: String,

    @ColumnInfo(name = "modification_date")
    val modificationDate: String,

    @ColumnInfo(name = "trash_date")
    val trashDate: String?,

    @ColumnInfo(name = "deleted_permanently")
    val deletedPermanently: Boolean
)

fun NoteEntity.toDomain(): Note {
    val gson = GsonBuilder()
        .registerTypeAdapter(AdvancedContentType::class.java, AdvancedContentTypeAdapter())
        .serializeNulls()
        .create()

    return Note(
        id = id,
        uuid = uuid,
        name = name,
        advancedContent = if (advancedContent != null) {
            gson.fromJson(advancedContent, Array<AdvancedContentItem>::class.java).toList()
        } else null,
        tagsUUIDs = if (tagsUUIDs != null) {
            gson.fromJson(tagsUUIDs, Array<String>::class.java).toList()
        } else emptyList(),
        location = if (location != null) {
            gson.fromJson(location, MyLocation::class.java)
        } else null,
        reminder = parseNoteReminderToDomain(reminder),
        fixed = fixed,
        archived = archived,
        creationDate = parseStringToDate(creationDate),
        modificationDate = parseStringToDate(modificationDate),
        trashDate = trashDate?.let { parseStringToDate(it) },
        deletedPermanently = deletedPermanently
    )
}