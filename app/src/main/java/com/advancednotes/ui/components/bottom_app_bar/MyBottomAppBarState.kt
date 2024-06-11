package com.advancednotes.ui.components.bottom_app_bar

import androidx.compose.runtime.Composable
import com.advancednotes.domain.models.BottomAppBarItem

data class MyBottomAppBarState(
    val actions: List<BottomAppBarItem> = emptyList(),
    val floatingActionButton: @Composable (() -> Unit)? = null,
    val bottomAppBarVisible: Boolean = true
)