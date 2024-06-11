package com.advancednotes.ui.components.chips

import android.content.res.Configuration
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.advancednotes.domain.models.MyChipColors
import com.advancednotes.ui.components.texts.MyText

@Composable
fun MyFilterChip(
    text: String,
    chipColors: MyChipColors = MyChipColors(),
    selected: Boolean,
    onClick: () -> Unit = {}
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            MyText(
                text = text,
                textStyle = MaterialTheme.typography.bodySmall
            )
        },
        leadingIcon = {
            if (selected) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                    Modifier.size(AssistChipDefaults.IconSize)
                )
            }
        },
        shape = RoundedCornerShape(100),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = chipColors.backgroundColor,
            labelColor = chipColors.textColor,
            iconColor = chipColors.textColor,
            disabledContainerColor = chipColors.backgroundColor,
            disabledLabelColor = chipColors.textColor,
            disabledLeadingIconColor = chipColors.textColor,
            disabledTrailingIconColor = chipColors.textColor,
            selectedContainerColor = chipColors.backgroundColor,
            disabledSelectedContainerColor = chipColors.backgroundColor,
            selectedLabelColor = chipColors.textColor,
            selectedLeadingIconColor = chipColors.textColor,
            selectedTrailingIconColor = chipColors.textColor,
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = chipColors.borderColor,
            selectedBorderColor = chipColors.borderColor,
            disabledBorderColor = chipColors.borderColor,
            disabledSelectedBorderColor = chipColors.borderColor,
            borderWidth = 1.dp,
            selectedBorderWidth = 1.dp,
        ),
    )
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun MyFilterChipPreviewLight() {
    MyFilterChip(
        "Preview",
        MyChipColors(
            backgroundColor = Color.White,
            textColor = Color.Black,
            borderColor = Color.Black
        ),
        true
    )
}

@Preview(
    name = "night_preview",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun MyFilterChipPreviewNight() {
    MyFilterChip(
        "Preview",
        MyChipColors(
            backgroundColor = Color.White,
            textColor = Color.Black,
            borderColor = Color.Black
        ),
        false
    )
}