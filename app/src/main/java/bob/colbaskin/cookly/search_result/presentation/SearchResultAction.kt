package bob.colbaskin.cookly.search_result.presentation

import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipesDisplayMode
import bob.colbaskin.cookly.search_result.domain.models.RecipeSearchFilters

sealed interface SearchResultAction {

    data object LoadInitialData: SearchResultAction
    data object Search: SearchResultAction

    data class ChangeSearchText(val value: String): SearchResultAction

    data object OpenFiltersSheet: SearchResultAction
    data object CloseFiltersSheet: SearchResultAction

    data class ChangeDraftFilters(val filters: RecipeSearchFilters): SearchResultAction

    data object ResetDraftFilters: SearchResultAction
    data object ApplyDraftFilters: SearchResultAction

    data class ChangeDisplayMode(val mode: RecipesDisplayMode): SearchResultAction

    data class OpenRecipe(val recipeId: Int): SearchResultAction
}
