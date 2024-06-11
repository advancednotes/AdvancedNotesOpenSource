package com.advancednotes.data.api.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SynchronizeNoteFileResponseModel(
    @SerializedName("noteFileToSynchronizeFromApi") val noteFileToSynchronizeFromApi: NoteFileModel?,
    @SerializedName("noteFileURIEndpoint") val noteFileURIEndpoint: String?,
)