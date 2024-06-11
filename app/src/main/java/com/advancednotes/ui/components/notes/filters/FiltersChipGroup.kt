package com.advancednotes.ui.components.notes.filters

import android.graphics.Color.parseColor
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.advancednotes.domain.models.MyChipColors
import com.advancednotes.domain.models.Tag
import com.advancednotes.ui.components.chips.MyFilterChip
import com.advancednotes.utils.colors.ColorsHelper.getAdaptiveTextColor

@Composable
fun FiltersChipGroup(
    usedTags: List<Tag>,
    selectedTagsUUIDs: List<String>,
    onClick: (tag: Tag) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        items(usedTags) { tag ->
            MyFilterChip(
                text = tag.name,
                chipColors = if (tag.colorHex != null) {
                    MyChipColors(
                        backgroundColor = Color(parseColor(tag.colorHex)),
                        textColor = Color(getAdaptiveTextColor(parseColor(tag.colorHex))),
                        borderColor = Color.Black
                    )
                } else MyChipColors(),
                selected = tag.uuid in selectedTagsUUIDs,
                onClick = {
                    onClick(tag)
                }
            )
        }
    }
}