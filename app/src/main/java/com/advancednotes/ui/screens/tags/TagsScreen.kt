package com.advancednotes.ui.screens.tags

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.advancednotes.R
import com.advancednotes.common.Constants.Companion.CONTENT_PADDING_DEFAULT
import com.advancednotes.domain.models.AdvancedFabMenuItem
import com.advancednotes.domain.models.Tag
import com.advancednotes.ui.components.ContentEmpty
import com.advancednotes.ui.components.ScreenContainer
import com.advancednotes.ui.components.lazy_column_draggable.LazyColumnDraggable
import com.advancednotes.ui.components.lazy_column_draggable.dragDropOffset
import com.advancednotes.ui.components.lazy_column_draggable.rememberDragDropListState
import com.advancednotes.ui.components.tags.TagItem
import com.advancednotes.ui.components.top_app_bar.MyTopAppBarState
import com.advancednotes.ui.screens.Screen
import com.advancednotes.ui.screens.main.MainActivityViewModel
import com.advancednotes.utils.navigation.myNavigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@Composable
fun TagsScreen(
    mainScope: CoroutineScope,
    navController: NavController,
    mainActivityViewModel: MainActivityViewModel,
    tagsViewModel: TagsViewModel = hiltViewModel()
) {
    val context: Context = LocalContext.current

    val tagsState: TagsState by tagsViewModel.tagsState.collectAsState()

    val dragDropListState = rememberDragDropListState(
        onMove = { fromIndex, toIndex ->
            tagsViewModel.onReorganize(fromIndex, toIndex)
        }
    )

    LaunchedEffect(Unit) {
        mainScope.launch {
            mainActivityViewModel.setCurrentRouteState(Screen.TagsScreen.route)
            mainActivityViewModel.setTopAppBarState(
                MyTopAppBarState(
                    label = context.getString(Screen.TagsScreen.labelResource),
                    navigationBack = true,
                    navigationBackAction = {
                        navController.popBackStack()
                    }
                )
            )
        }

        tagsViewModel.getTags()
    }

    LaunchedEffect(tagsState.isLoading) {
        if (!tagsState.isLoading) {
            mainScope.launch {
                val advancedFabMenuVerticalItems: List<AdvancedFabMenuItem> =
                    listOf(
                        AdvancedFabMenuItem(
                            icon = Icons.Default.Add,
                            text = context.getString(R.string.add_new_tag),
                            onClick = {
                                navController.myNavigate(
                                    route = Screen.SingleTagScreen.route,
                                    afterNavigate = { mainActivityViewModel.clearAdvancedFabMenuState() }
                                )
                            }
                        )
                    )

                mainActivityViewModel.setAdvancedFabMenuItems(verticalItems = advancedFabMenuVerticalItems)
            }
        }
    }

    LaunchedEffect(tagsViewModel.refreshTagsChannel) {
        tagsViewModel.refreshTagsChannel.receiveAsFlow().collectLatest {
            tagsViewModel.getTags()
        }
    }

    ScreenContainer(
        topPadding = false,
        bottomPadding = false
    ) {
        if (tagsState.tagsWithNotesCount.isNotEmpty()) {
            LazyColumnDraggable(
                dragDropListState = dragDropListState,
                contentPadding = PaddingValues(vertical = CONTENT_PADDING_DEFAULT),
                verticalArrangement = Arrangement.spacedBy(CONTENT_PADDING_DEFAULT)
            ) {
                itemsIndexed(tagsState.tagsWithNotesCount) { index, tagWithNotesCount ->
                    val (tag: Tag, notesCount: Int) = tagWithNotesCount

                    TagItem(
                        tag = tag,
                        notesCount = notesCount,
                        onClick = {
                            navController.myNavigate(
                                route = Screen.SingleTagScreen.route + "?tagUUID=${tag.uuid}",
                                afterNavigate = { mainActivityViewModel.clearAdvancedFabMenuState() }
                            )
                        },
                        modifier = Modifier
                            .dragDropOffset(
                                dragDropListState = dragDropListState,
                                index = index
                            )
                    )
                }
            }
        } else {
            if (!tagsState.isLoading) {
                ContentEmpty(text = stringResource(id = R.string.content_empty_tags))
            }
        }
    }
}