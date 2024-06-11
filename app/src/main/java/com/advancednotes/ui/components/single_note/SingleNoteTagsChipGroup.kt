package com.advancednotes.ui.components.single_note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.advancednotes.domain.models.MyChipColors
import com.advancednotes.domain.models.Tag
import com.advancednotes.ui.components.chips.MyAssistChip
import com.advancednotes.utils.colors.ColorsHelper

@Composable
fun SingleNoteTagsChipGroup(
    tags: List<Tag>,
    tagsUUIDs: List<String>
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        val tagsAux: List<Tag> = tags.filter { tagFilter -> tagsUUIDs.any { tagFilter.uuid == it } }

        items(tagsAux) { tag ->
            MyAssistChip(
                text = tag.name,
                chipColors = if (tag.colorHex != null) {
                    MyChipColors(
                        backgroundColor = Color(android.graphics.Color.parseColor(tag.colorHex)),
                        textColor = Color(
                            ColorsHelper.getAdaptiveTextColor(
                                android.graphics.Color.parseColor(
                                    tag.colorHex
                                )
                            )
                        ),
                        borderColor = Color.Black
                    )
                } else MyChipColors()
            )
        }
    }
}