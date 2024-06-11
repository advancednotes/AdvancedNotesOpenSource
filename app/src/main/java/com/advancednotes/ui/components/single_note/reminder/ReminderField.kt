package com.advancednotes.ui.components.single_note.reminder

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.advancednotes.ui.components.cards.AdvancedStyledCard
import com.advancednotes.ui.components.cards.AdvancedStyledCardIntensityLevel
import com.advancednotes.ui.components.texts.MyText

@Composable
fun ReminderField(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        AdvancedStyledCard(
            intensityLevel = AdvancedStyledCardIntensityLevel.LOW
        ) {
            Box(
                modifier = Modifier
                    .clickable {
                        onClick()
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                MyText(
                    text = text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
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
fun ReminderFieldPreviewLight() {
    ReminderField(
        icon = Icons.Filled.Map,
        text = "Preview",
        onClick = {}
    )
}