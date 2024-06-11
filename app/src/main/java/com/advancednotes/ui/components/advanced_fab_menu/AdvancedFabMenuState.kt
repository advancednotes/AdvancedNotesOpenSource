package com.advancednotes.ui.components.advanced_fab_menu

import com.advancednotes.domain.models.AdvancedFabMenuItem

data class AdvancedFabMenuState(
    val expandedOnCreate: Boolean = false,
    val verticalItems: List<AdvancedFabMenuItem> = emptyList(),
    val horizontalItems: List<AdvancedFabMenuItem> = emptyList(),
)