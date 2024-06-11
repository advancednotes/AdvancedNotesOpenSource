package com.advancednotes.domain.models

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationDrawerItem(
    val route: String,
    val title: String,
    val contentDescription: String,
    val icon: ImageVector,
    val count: Int? = null,
)
