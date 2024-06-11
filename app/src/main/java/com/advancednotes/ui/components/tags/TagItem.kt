package com.advancednotes.ui.components.tags

import android.graphics.Color.parseColor
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.advancednotes.domain.models.Tag
import com.advancednotes.ui.components.cards.MyElevatedCardStyled
import com.advancednotes.ui.components.texts.MyText
import com.advancednotes.utils.colors.ColorsHelper.getAdaptiveTextColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TagItem(
    tag: Tag,
    notesCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MyElevatedCardStyled(
        modifier = modifier,
        colors = if (tag.colorHex != null) {
            CardDefaults.elevatedCardColors(
                containerColor = Color(parseColor(tag.colorHex))
            )
        } else CardDefaults.elevatedCardColors()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 42.dp)
                .combinedClickable(
                    onClick = {
                        onClick.invoke()
                    }
                )
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DragIndicator,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = tag.colorHex?.let {
                    Color(getAdaptiveTextColor(parseColor(tag.colorHex)))
                } ?: LocalContentColor.current
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalAlignment = Alignment.Top
            ) {
                MyText(
                    text = tag.name,
                    modifier = Modifier.weight(1f),
                    color = tag.colorHex?.let { Color(getAdaptiveTextColor(parseColor(tag.colorHex))) },
                    textStyle = MaterialTheme.typography.bodyMedium
                )

                MyText(
                    text = notesCount.toString(),
                    color = tag.colorHex?.let { Color(getAdaptiveTextColor(parseColor(tag.colorHex))) },
                    textStyle = if (notesCount != 0) {
                        MaterialTheme.typography.titleSmall
                    } else MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}