package com.advancednotes.ui.components.notes.filters

import com.advancednotes.domain.models.Tag

data class FiltersState(
    val searchQuery: String = "",
    val orderBy: String = "ASC",
    val usedTags: List<Tag> = emptyList(),
    val selectedTagsUUIDs: List<String> = emptyList(),
)