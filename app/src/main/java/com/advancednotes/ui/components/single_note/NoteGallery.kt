package com.advancednotes.ui.components.single_note

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.advancednotes.domain.models.NoteFile
import com.advancednotes.ui.screens.Screen
import com.advancednotes.ui.screens.main.MainActivityViewModel
import com.advancednotes.ui.screens.single_note.SingleNoteViewModel
import com.advancednotes.utils.navigation.myNavigate

@Composable
fun NoteGallery(
    navController: NavController,
    noteImages: List<NoteFile>,
    mainActivityViewModel: MainActivityViewModel,
    singleNoteViewModel: SingleNoteViewModel,
) {
    val context = LocalContext.current

    LazyVerticalGrid(
        modifier = Modifier.heightIn(min = 0.dp, max = 300.dp),
        columns = GridCells.Adaptive(minSize = 100.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(noteImages) { item ->
            singleNoteViewModel.getPathFromNoteFile(item)?.let {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(it)
                        .crossfade(true)
                        .size(150, 150)
                        .scale(Scale.FILL)
                        .build(),
                    placeholder = null,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable {
                            navController.myNavigate(
                                route = Screen.NoteGalleryScreen.route + "/${item.uuid}",
                                afterNavigate = { mainActivityViewModel.clearAdvancedFabMenuState() }
                            )
                        }
                )
            }
        }
    }
}