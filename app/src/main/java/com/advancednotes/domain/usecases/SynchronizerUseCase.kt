package com.advancednotes.domain.usecases

interface SynchronizerUseCase {

    fun synchronizeNotes(
        notesUUIDs: List<String>,
        onComplete: (() -> Unit)? = null
    )

    fun synchronizeNotesFiles(
        notesFilesUUIDs: List<String>,
        onComplete: (() -> Unit)? = null
    )

    fun synchronizeTags(
        tagsUUIDs: List<String>,
        onComplete: (() -> Unit)? = null
    )
}