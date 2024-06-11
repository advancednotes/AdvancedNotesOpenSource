package com.advancednotes.ui.components.cards

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun MyElevatedCardStyled(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    colors: CardColors = CardDefaults.elevatedCardColors(),
    elevation: CardElevation = CardDefaults.elevatedCardElevation(),
    content: @Composable() (ColumnScope.() -> Unit)
) {
    val borderColors = listOf(
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
    )

    MyElevatedCard(
        modifier = modifier
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(colors = borderColors),
                shape = shape
            ),
        shape = shape,
        colors = colors,
        elevation = elevation
    ) {
        content()
    }
}