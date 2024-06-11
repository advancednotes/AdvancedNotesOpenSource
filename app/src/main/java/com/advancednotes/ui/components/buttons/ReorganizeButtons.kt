package com.advancednotes.ui.components.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable

@Composable
fun ReorganizeButtons(
    onReorganizeUp: () -> Unit,
    onReorganizeDown: () -> Unit,
) {
    Column {
        MyIconButton(
            icon = Icons.Default.ArrowDropUp,
            onClick = onReorganizeUp
        )

        MyIconButton(
            icon = Icons.Default.ArrowDropDown,
            onClick = onReorganizeDown
        )
    }
}