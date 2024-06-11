package com.advancednotes.domain.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class AdvancedFabMenuItem(
    val icon: ImageVector,
    val text: String,
    val onClick: () -> Unit,
    val iconTint: Color? = null
)