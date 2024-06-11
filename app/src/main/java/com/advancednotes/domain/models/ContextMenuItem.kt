package com.advancednotes.domain.models

import androidx.compose.ui.graphics.vector.ImageVector

data class ContextMenuItem(
    val text: String,
    val onClick: () -> Unit,
    val icon: ImageVector? = null,
)