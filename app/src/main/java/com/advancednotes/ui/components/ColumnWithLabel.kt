package com.advancednotes.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.advancednotes.common.Constants
import com.advancednotes.ui.components.texts.MyTextAnimated

@Composable
fun ColumnWithLabel(
    label: String,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(Constants.CONTENT_PADDING_DEFAULT),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        MyTextAnimated(
            text = label,
            textStyle = MaterialTheme.typography.labelMedium
        )

        content()
    }
}