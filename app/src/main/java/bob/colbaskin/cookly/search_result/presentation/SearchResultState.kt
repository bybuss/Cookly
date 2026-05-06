package bob.colbaskin.cookly.search_result.presentation

import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipesDisplayMode
import bob.colbaskin.cookly.onboarding_preferences.domain.models.IngredientGroup
import bob.colbaskin.cookly.search_result.domain.models.RecipeSearchFilters

data class SearchResultState(
    val searchText: String = "",
    val appliedFilters: RecipeSearchFilters = RecipeSearchFilters(),
    val draftFilters: RecipeSearchFilters = RecipeSearchFilters(),

    val ingredientGroupsState: UiState<List<IngredientGroup>> = UiState.Idle,
    val recipesState: UiState<List<RecipePreview>> = UiState.Idle,

    val displayMode: RecipesDisplayMode = RecipesDisplayMode.Banners,
    val isFiltersSheetVisible: Boolean = false
)
