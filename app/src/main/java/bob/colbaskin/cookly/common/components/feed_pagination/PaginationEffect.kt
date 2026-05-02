package bob.colbaskin.cookly.common.components.feed_pagination

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import bob.colbaskin.cookly.common.UiState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PaginationEffect(
    gridState: LazyGridState,
    itemCount: Int,
    appendState: UiState<Unit>,
    isEndReached: Boolean,
    enabled: Boolean,
    preloadDistance: Int = 5,
    onLoadNext: () -> Unit
) {
    LaunchedEffect(gridState, itemCount, appendState, isEndReached, enabled, preloadDistance) {
        snapshotFlow {
            val lastVisibleItem = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val totalItems = gridState.layoutInfo.totalItemsCount
            lastVisibleItem to totalItems
        }.collectLatest { (lastVisibleItem, totalItems) ->
            if (
                enabled &&
                lastVisibleItem != null &&
                totalItems > 0 &&
                lastVisibleItem >= totalItems - preloadDistance &&
                !isEndReached &&
                appendState !is UiState.Loading
            ) {
                onLoadNext()
            }
        }
    }
}
