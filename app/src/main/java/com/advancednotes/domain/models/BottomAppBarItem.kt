package com.advancednotes.domain.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomAppBarItem(
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit,
    val iconTint: Color? = null
)