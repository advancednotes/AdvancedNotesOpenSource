package com.advancednotes.ui.components.single_note.advanced_content

import com.advancednotes.domain.models.AdvancedContentItemUI

data class AdvancedContentUIState(
    val advancedContentUI: List<AdvancedContentItemUI> = mutableListOf(),
    var focusedIndex: Int = -1
)