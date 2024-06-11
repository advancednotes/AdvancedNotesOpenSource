package com.advancednotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.advancednotes.common.Constants.Companion.HORIZONTAL_PADDING
import com.advancednotes.common.Constants.Companion.VERTICAL_PADDING

@Composable
fun ScreenContainer(
    modifier: Modifier = Modifier,
    startPadding: Boolean = true,
    topPadding: Boolean = true,
    endPadding: Boolean = true,
    bottomPadding: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val paddingModifier = Modifier
        .padding(
            start = if (startPadding) HORIZONTAL_PADDING else 0.dp,
            top = if (topPadding) VERTICAL_PADDING else 0.dp,
            end = if (endPadding) HORIZONTAL_PADDING else 0.dp,
            bottom = if (bottomPadding) VERTICAL_PADDING else 0.dp
        )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .then(paddingModifier)
        ) {
            content()
        }
    }
}