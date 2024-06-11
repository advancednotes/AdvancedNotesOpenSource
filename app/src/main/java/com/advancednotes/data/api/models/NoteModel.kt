package com.advancednotes.data.api.models

import androidx.annotation.Keep
import com.advancednotes.domain.models.AdvancedContentItem
import com.advancednotes.domain.models.AdvancedContentType
import com.advancednotes.domain.models.MyLocation
import com.advancednotes.domain.models.Note
import com.advancednotes.domain.models.parseNoteReminderToDomain
import com.advancednotes.utils.date.DateHelper.parseStringToDate
import com.advancednotes.utils.type_adapters.AdvancedContentTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

@Keep
data class NoteModel(
    @SerializedName("id") val id: Long,
    @SerializedName("uuid") val uuid: String,
    @SerializedName("name") val name: String,
    @SerializedName("advancedContent") val advancedContent: String?,
    @SerializedName("tagsUUIDs") val tagsUUIDs: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("reminder") val reminder: String?,
    @SerializedName("fixed") val fixed: Boolean,
    @SerializedName("archived") val archived: Boolean,
    @SerializedName("creationDate") val creationDate: String,
    @SerializedName("modificationDate") val modificationDate: String,
    @SerializedName("trashDate") val trashDate: String?,
    @SerializedName("deletedPermanently") val deletedPermanently: Boolean,
)

fun NoteModel.toDomain(): Note {
    val gson: Gson = GsonBuilder()
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