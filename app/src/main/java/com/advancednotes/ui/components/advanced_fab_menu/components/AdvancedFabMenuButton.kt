package com.advancednotes.ui.components.advanced_fab_menu.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.advancednotes.ui.components.texts.MyText

@Composable
fun AdvancedFabMenuButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null
) {
    val backgroundColors = listOf(
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
    )

    val backgroundModifier = if (text == null) {
        Modifier.background(
            brush = Brush.linearGradient(colors = backgroundColors)
        )
    } else Modifier

    ElevatedCard(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.large
    ) {
        Box(
            modifier = Modifier
                .defaultMinSize(
                    minWidth = 60.dp,
                    minHeight = 60.dp
                )
                .then(backgroundModifier),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )

                if (text != null) {
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))

                    MyText(
                        text = text,
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun AdvancedFabMenuButtonPreviewLight() {
    AdvancedFabMenuButton(
        icon = Icons.Default.Add,
        onClick = {

        }
    )
}