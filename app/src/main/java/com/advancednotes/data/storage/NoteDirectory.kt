package com.advancednotes.data.storage

enum class NoteDirectory(val path: String) {
    IMAGES(path = "images"),
    AUDIO(path = "audio"),
    DOCUMENTS(path = "documents"),
    OTHER(path = "other"),
}

fun getNoteDirectoryByPath(path: String): NoteDirectory? {
    return NoteDirectory.values().find { it.path == path }
}