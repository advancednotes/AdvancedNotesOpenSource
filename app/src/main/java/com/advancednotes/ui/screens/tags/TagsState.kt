package com.advancednotes.ui.screens.tags

import com.advancednotes.domain.models.Tag

data class TagsState(
    val isLoading: Boolean = true,
    val tagsWithNotesCount: List<Pair<Tag, Int>> = emptyList()
)