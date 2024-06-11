package com.advancednotes.ui.components.cards

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AdvancedStyledCard(
    modifier: Modifier = Modifier,
    intensityLevel: AdvancedStyledCardIntensityLevel = AdvancedStyledCardIntensityLevel.MIDDLE,
    shape: Shape = MaterialTheme.shapes.small,
    content: @Composable BoxScope.() -> Unit
) {
    val borderAlpha = when (intensityLevel) {
        AdvancedStyledCardIntensityLevel.LOW -> 0.3f
        AdvancedStyledCardIntensityLevel.MIDDLE -> 0.5f
        AdvancedStyledCardIntensityLevel.HIGH -> 0.7f
    }

    val backgroundAlpha = when (intensityLevel) {
        AdvancedStyledCardIntensityLevel.LOW -> 0.2f
        AdvancedStyledCardIntensityLevel.MIDDLE -> 0.4f
        AdvancedStyledCardIntensityLevel.HIGH -> 0.6f
    }

    val borderColors = listOf(
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = borderAlpha),
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = borderAlpha),
        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = borderAlpha)
    )

    val backgroundColors = listOf(
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = backgroundAlpha),
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = backgroundAlpha),
        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = backgroundAlpha)
    )

    Box(
        modifier = modifier
            .clip(
                shape = shape
            )
            .background(
                brush = Brush.linearGradient(colors = backgroundColors)
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(colors = borderColors),
                shape = shape
            )
    ) {
        content()
    }
}

enum class AdvancedStyledCardIntensityLevel {
    LOW,
    MIDDLE,
    HIGH
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun AdvancedStyledCardPreviewLight() {
    AdvancedStyledCard(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}