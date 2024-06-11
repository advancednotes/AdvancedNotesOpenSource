package com.advancednotes.domain.usecases

interface PreSynchronizerUseCase {

    fun preSynchronizeNotes(
        onComplete: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    )

    fun preSynchronizeNotesFiles(
        onComplete: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    )

    fun preSynchronizeTags(
        onComplete: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    )
}