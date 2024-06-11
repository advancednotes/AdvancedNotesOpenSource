package com.advancednotes.domain.models

import androidx.compose.ui.graphics.vector.ImageVector

data class TopAppBarItem(
    val icon: ImageVector,
    val text: String,
    val onClick: () -> Unit
)