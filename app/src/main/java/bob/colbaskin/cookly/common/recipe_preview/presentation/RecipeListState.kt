package bob.colbaskin.cookly.common.recipe_preview.presentation

import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipesDisplayMode

data class RecipeListState(
    val recipesState: UiState<List<RecipePreview>> = UiState.Idle,
    val displayMode: RecipesDisplayMode = RecipesDisplayMode.Banners
)
