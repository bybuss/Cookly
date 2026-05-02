package bob.colbaskin.cookly.common.components.feed_pagination

import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.toUiState
import bob.colbaskin.cookly.home.domain.models.main.FeedPage
import bob.colbaskin.cookly.home.domain.models.main.FeedRecipe

class FeedPaginator(
    private val onLoad: suspend (
        lastScore: Double?,
        lastId: Int?,
        paginationKey: String?
    ) -> ApiResult<FeedPage>
) {

    suspend fun loadFirst(): PaginationState<FeedRecipe> {
        return when (val result = onLoad(null, null, null)) {
            is ApiResult.Success -> {
                val page = result.data
                PaginationState(
                    items = page.recipes,
                    loadState = UiState.Success(page.recipes),
                    lastScore = page.lastRecipeScore,
                    lastId = page.lastRecipeId,
                    paginationKey = page.paginationKey,
                    isEndReached = page.paginationKey == null
                )
            }

            is ApiResult.Error -> {
                PaginationState(loadState = result.toUiState())
            }
        }
    }

    suspend fun loadNext(state: PaginationState<FeedRecipe>): PaginationState<FeedRecipe> {
        if (state.paginationKey == null) return state

        return when (
            val result = onLoad(
                state.lastScore,
                state.lastId,
                state.paginationKey
            )
        ) {
            is ApiResult.Success -> {
                val page = result.data

                val merged = (state.items + page.recipes)
                    .distinctBy { it.id }

                state.copy(
                    items = merged,
                    loadState = UiState.Success(merged),
                    appendState = UiState.Success(Unit),
                    lastScore = page.lastRecipeScore,
                    lastId = page.lastRecipeId,
                    paginationKey = page.paginationKey,
                    isEndReached = page.paginationKey == null
                )
            }

            is ApiResult.Error -> {
                state.copy(appendState = result.toUiState())
            }
        }
    }
}
