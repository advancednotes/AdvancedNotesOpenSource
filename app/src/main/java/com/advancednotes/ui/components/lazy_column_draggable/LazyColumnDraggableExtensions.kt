package com.advancednotes.ui.components.lazy_column_draggable

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex

fun LazyListState.getVisibleItemInfoFor(absolute: Int): LazyListItemInfo? {
    return this.layoutInfo.visibleItemsInfo.getOrNull(absolute - this.layoutInfo.visibleItemsInfo.first().index)
}

val LazyListItemInfo.offsetEnd: Int
    get() = this.offset + this.size

fun Modifier.dragDropOffset(
    dragDropListState: DragDropListState,
    index: Int
): Modifier = composed {
    val offsetOrNull = dragDropListState.elementDisplacement.takeIf {
        index == dragDropListState.currentIndexOfDraggedItem
    }

    val zIndex = if (offsetOrNull != null) 1f else 0.5f

    this.then(
        Modifier
            .graphicsLayer {
                translationY = offsetOrNull ?: 0f
            }
            .zIndex(zIndex)
    )
}