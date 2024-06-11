package com.advancednotes.ui.components.single_tag

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.advancednotes.domain.models.Note
import com.advancednotes.ui.components.cards.MyElevatedCardStyled
import com.advancednotes.ui.components.texts.MyText

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingleTagNoteItem(
    note: Note,
    onClick: () -> Unit
) {
    MyElevatedCardStyled(
        modifier = Modifier
            .height(60.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = {
                        onClick.invoke()
                    }
                )
                .padding(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalAlignment = Alignment.Top
            ) {
                MyText(
                    text = note.name,
                    textStyle = MaterialTheme.typography.labelMedium
                )

                Spacer(modifier = Modifier.weight(1f))

                if (note.fixed) {
                    Icon(
                        imageVector = Icons.Filled.PushPin,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = false
)
@Composable
fun SingleTagNoteItemPreviewLight() {
    SingleTagNoteItem(
        note = Note(name = "Preview"),
        onClick = {}
    )
}