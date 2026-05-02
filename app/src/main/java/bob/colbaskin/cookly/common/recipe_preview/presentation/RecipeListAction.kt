package bob.colbaskin.cookly.common.recipe_preview.presentation

import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipesDisplayMode

sealed interface RecipeListAction {
    data object LoadRecipes : RecipeListAction
    data class ChangeDisplayMode(val mode: RecipesDisplayMode) : RecipeListAction
    data class OpenRecipe(val id: Int) : RecipeListAction
}
