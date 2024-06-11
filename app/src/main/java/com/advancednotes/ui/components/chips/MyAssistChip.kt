package com.advancednotes.ui.components.chips

import android.content.res.Configuration
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.advancednotes.domain.models.MyChipColors
import com.advancednotes.ui.components.texts.MyText

@Composable
fun MyAssistChip(
    text: String,
    icon: ImageVector? = null,
    chipColors: MyChipColors? = null,
    onClick: () -> Unit = {}
) {
    AssistChip(
        onClick = onClick,
        label = {
            MyText(
                text = text,
                textStyle = MaterialTheme.typography.bodySmall
            )
        },
        leadingIcon = {
            if (icon != null) {
                Icon(
                    Icons.Filled.Notifications,
                    contentDescription = null,
                    Modifier.size(AssistChipDefaults.IconSize)
                )
            }
        },
        shape = RoundedCornerShape(100),
        colors = if (chipColors != null) {
            AssistChipDefaults.assistChipColors(
                containerColor = chipColors.backgroundColor,
                labelColor = chipColors.textColor,
                leadingIconContentColor = chipColors.textColor,
                trailingIconContentColor = chipColors.textColor,
                disabledContainerColor = chipColors.backgroundColor,
                disabledLabelColor = chipColors.textColor,
                disabledLeadingIconContentColor = chipColors.textColor,
                disabledTrailingIconContentColor = chipColors.textColor,
            )
        } else AssistChipDefaults.assistChipColors(),
        border = if (chipColors != null) {
            AssistChipDefaults.assistChipBorder(
                enabled = true,
                borderColor = chipColors.borderColor,
                disabledBorderColor = chipColors.borderColor,
                borderWidth = 1.dp
            )
        } else AssistChipDefaults.assistChipBorder(enabled = true),
    )
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun MyAssistChipPreviewLight() {
    MyAssistChip("Preview", Icons.Filled.Notifications)
}

@Preview(
    name = "night_preview",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun MyAssistChipPreviewNight() {
    MyAssistChip("Preview")
}