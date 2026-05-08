package bob.colbaskin.cookly.profile.presentation.recipe_statuses

import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipesDisplayMode

sealed interface RecipeStatusesAction {

    data class SelectTab(val tab: RecipeStatusesTab): RecipeStatusesAction

    data class LoadTab(
        val tab: RecipeStatusesTab,
        val forceRefresh: Boolean = false
    ): RecipeStatusesAction

    data class RefreshTab(val tab: RecipeStatusesTab): RecipeStatusesAction

    data class ChangeDisplayMode(
        val tab: RecipeStatusesTab,
        val mode: RecipesDisplayMode
    ): RecipeStatusesAction

    data class OpenRecipe(val recipeId: Int): RecipeStatusesAction
}
