package com.advancednotes.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.advancednotes.ui.theme.Red

@Composable
fun ColorItem(
    color: Color,
    iconColor: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .size(width = 60.dp, height = 60.dp)
            .clip(MaterialTheme.shapes.small)
            .background(color)
            .clickable(
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Icon(
                modifier = Modifier
                    .size(width = 40.dp, height = 40.dp),
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = iconColor
            )
        }
    }
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun ColorItemPreviewLight() {
    ColorItem(
        color = Red,
        iconColor = Color.Black,
        selected = true,
        onClick = {}
    )
}

@Preview(
    name = "night_preview",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun ColorItemPreviewNight() {
    ColorItem(color = Red,
        iconColor = Color.Black,
        selected = true,
        onClick = {}
    )
}