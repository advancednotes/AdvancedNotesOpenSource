package com.advancednotes.ui.components.chips

import android.content.res.Configuration
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.advancednotes.ui.components.texts.MyText
import com.advancednotes.ui.theme.YellowReminder

@Composable
fun ReminderChip(text: String, dateIsOld: Boolean, modifier: Modifier = Modifier) {
    AssistChip(
        onClick = {},
        label = {
            MyText(
                text = text,
                textStyle = if (dateIsOld) {
                    MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough)
                } else MaterialTheme.typography.bodySmall
            )
        },
        modifier = modifier
            .height(32.dp)
            .alpha(if (dateIsOld) 0.5f else 1f),
        enabled = false,
        leadingIcon = {
            Icon(
                Icons.Filled.Notifications,
                contentDescription = null,
                Modifier.size(AssistChipDefaults.IconSize)
            )
        },
        shape = RoundedCornerShape(100),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = YellowReminder,
            labelColor = Color.Black,
            leadingIconContentColor = Color.Black,
            trailingIconContentColor = Color.Black,
            disabledContainerColor = YellowReminder,
            disabledLabelColor = Color.Black,
            disabledLeadingIconContentColor = Color.Black,
            disabledTrailingIconContentColor = Color.Black,
        ),
        border = AssistChipDefaults.assistChipBorder(
            enabled = true,
            borderColor = Color.Black,
            disabledBorderColor = Color.Black,
            borderWidth = 1.dp,
        )
    )
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun ReminderChipPreviewLight() {
    ReminderChip("Preview", false)
}

@Preview(
    name = "night_preview",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun ReminderChipPreviewNight() {
    ReminderChip("Preview", true)
}