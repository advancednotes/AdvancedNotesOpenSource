package com.advancednotes.ui.components.top_app_bar

import com.advancednotes.domain.models.TopAppBarItem

data class MyTopAppBarState(
    val label: String = "",
    val actions: List<TopAppBarItem> = emptyList(),
    val menuActions: List<TopAppBarItem> = emptyList(),
    val navigationBack: Boolean = true,
    val navigationBackAction: (() -> Unit)? = null
)