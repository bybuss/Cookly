package bob.colbaskin.cookly.home.presentation.recipe_detailed

interface RecipeDetailedAction {
    data object NavigateBack: RecipeDetailedAction
    data object StartCook: RecipeDetailedAction
    data object AddIngredientsToCart: RecipeDetailedAction
    data object ToggleLike: RecipeDetailedAction
    data object AddToCart: RecipeDetailedAction
    data class OnSheetStateChanged(val isExpanded: Boolean) : RecipeDetailedAction
}
