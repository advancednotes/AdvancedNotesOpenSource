package com.advancednotes.ui.components

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.advancednotes.ui.components.texts.MyText

@Composable
fun MyAccordion(
    title: String,
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
    expandable: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    var isExpanded: Boolean by rememberSaveable { mutableStateOf(expanded) }

    val clickableModifier = if (expandable) {
        Modifier.clickable(
            onClick = { isExpanded = !isExpanded }
        )
    } else {
        Modifier
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                shape = MaterialTheme.shapes.small
            )
    ) {
        Column(
            modifier = clickableModifier
        ) {
            Row(
                modifier = Modifier
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalAlignment = Alignment.Top
            ) {
                MyText(
                    text = title,
                    modifier = Modifier.weight(1f)
                )

                if (expandable) {
                    Icon(
                        if (isExpanded) {
                            Icons.Default.KeyboardArrowUp
                        } else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            MyHorizontalDivider()
        }

        val heightModifier: Modifier = if (isExpanded) {
            Modifier.wrapContentHeight()
        } else Modifier.height(0.dp)

        Box(
            modifier = Modifier
                .animateContentSize()
                .padding(all = 10.dp)
                .then(heightModifier)
        ) {
            content()
        }
    }
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun DropdownPreviewLight() {
    MyAccordion("Preview") {
        MyText(text = "Preview content")
    }
}

@Preview(
    name = "night_preview",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun DropdownPreviewNight() {
    MyAccordion("Preview", expanded = false) {
        MyText(text = "Preview content")
    }
}