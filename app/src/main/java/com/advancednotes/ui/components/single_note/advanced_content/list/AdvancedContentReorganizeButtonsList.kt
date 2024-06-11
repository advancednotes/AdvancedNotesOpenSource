package com.advancednotes.ui.components.single_note.advanced_content.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.advancednotes.ui.components.buttons.MyIconButton

@Composable
fun AdvancedContentReorganizeButtonsList(
    onReorganizeUp: () -> Unit,
    onReorganizeDown: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyIconButton(
            icon = Icons.Default.ArrowDropUp,
            onClick = onReorganizeUp,
            modifier = Modifier.size(30.dp),
            iconSize = 28.dp
        )

        MyIconButton(
            icon = Icons.Default.ArrowDropDown,
            onClick = onReorganizeDown,
            modifier = Modifier.size(30.dp),
            iconSize = 28.dp
        )
    }
}