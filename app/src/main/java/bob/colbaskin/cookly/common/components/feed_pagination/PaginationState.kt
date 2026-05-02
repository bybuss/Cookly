package bob.colbaskin.cookly.common.components.feed_pagination

import bob.colbaskin.cookly.common.UiState

data class PaginationState<T>(
    val items: List<T> = emptyList(),
    val loadState: UiState<List<T>> = UiState.Idle,
    val appendState: UiState<Unit> = UiState.Idle,
    val lastScore: Double? = null,
    val lastId: Int? = null,
    val paginationKey: String? = null,
    val isEndReached: Boolean = false
)
