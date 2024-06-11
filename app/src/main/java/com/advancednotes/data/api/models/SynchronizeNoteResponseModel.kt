package com.advancednotes.data.api.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SynchronizeNoteResponseModel(
    @SerializedName("noteToSynchronizeFromApi") val noteToSynchronizeFromApi: NoteModel?
)