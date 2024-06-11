package com.advancednotes.data.api.repositories

import com.advancednotes.data.api.models.PreSynchronizeItem
import com.advancednotes.data.api.models.PreSynchronizeNotesFilesResponseModel
import com.advancednotes.data.api.models.PreSynchronizeNotesResponseModel
import com.advancednotes.data.api.models.PreSynchronizeTagsResponseModel

interface ApiPreSynchronizerRepository {

    fun preSynchronizeNotes(
        notesToPreSynchronize: List<PreSynchronizeItem>,
        onResponse: (response: PreSynchronizeNotesResponseModel?) -> Unit,
        onBadRequest: (() -> Unit)? = null,
        onAccessTokenNotValid: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    )

    fun preSynchronizeNotesFiles(
        notesFilesToPreSynchronize: List<PreSynchronizeItem>,
        onResponse: (response: PreSynchronizeNotesFilesResponseModel?) -> Unit,
        onBadRequest: (() -> Unit)? = null,
        onAccessTokenNotValid: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    )

    fun preSynchronizeTags(
        tagsToPreSynchronize: List<PreSynchronizeItem>,
        onResponse: (response: PreSynchronizeTagsResponseModel?) -> Unit,
        onBadRequest: (() -> Unit)? = null,
        onAccessTokenNotValid: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    )
}