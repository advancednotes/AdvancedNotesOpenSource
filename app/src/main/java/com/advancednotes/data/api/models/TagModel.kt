package com.advancednotes.data.api.models

import androidx.annotation.Keep
import com.advancednotes.domain.models.Tag
import com.advancednotes.utils.date.DateHelper.parseStringToDate
import com.google.gson.annotations.SerializedName

@Keep
data class TagModel(
    @SerializedName("id") val id: Long,
    @SerializedName("uuid") val uuid: String,
    @SerializedName("name") val name: String,
    @SerializedName("colorHex") val colorHex: String?,
    @SerializedName("orderNumber") val orderNumber: Int,
    @SerializedName("creationDate") val creationDate: String,
    @SerializedName("modificationDate") val modificationDate: String,
    @SerializedName("deletedPermanently") var deletedPermanently: Boolean
)

fun TagModel.toDomain(): Tag {
    return Tag(
        id = id,
        uuid = uuid,
        name = name,
        colorHex = colorHex,
        orderNumber = orderNumber,
        creationDate = parseStringToDate(creationDate),
        modificationDate = parseStringToDate(modificationDate),
        deletedPermanently = deletedPermanently
    )
}