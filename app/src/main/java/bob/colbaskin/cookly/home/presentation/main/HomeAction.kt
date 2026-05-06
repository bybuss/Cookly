package bob.colbaskin.cookly.home.presentation.main

import bob.colbaskin.cookly.search_result.domain.models.RecipeSearchFilters

interface HomeAction {
    data object LoadInitialFeed: HomeAction
    data object RefreshFeed: HomeAction
    data object LoadNextFeedPage: HomeAction

    data class OpenRecipe(val recipeId: Int): HomeAction
    data class OpenMealTimeDetailed(val mealTimeType: String): HomeAction

    data class CancelActiveSession(val sessionId: Int): HomeAction

    data class ChangeSearchText(val value: String): HomeAction
    data object Search: HomeAction

    data object OpenFiltersSheet: HomeAction
    data object CloseFiltersSheet: HomeAction
    data class ChangeDraftSearchFilters(val filters: RecipeSearchFilters): HomeAction
    data object ResetDraftSearchFilters: HomeAction
    data object ApplyDraftSearchFilters: HomeAction

    data class OpenSearchResult(val filters: RecipeSearchFilters): HomeAction
}
