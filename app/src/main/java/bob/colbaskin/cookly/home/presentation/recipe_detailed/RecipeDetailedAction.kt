package bob.colbaskin.cookly.home.presentation.recipe_detailed

interface RecipeDetailedAction {
    data object NavigateBack: RecipeDetailedAction
    data object NavigateMain: RecipeDetailedAction
    data object StartCook: RecipeDetailedAction
    data object ToggleLike: RecipeDetailedAction

    data object ShowAddToCartSheet: RecipeDetailedAction
    data object HideAddToCartSheet: RecipeDetailedAction
    data object IncreasePortions: RecipeDetailedAction
    data object DecreasePortions: RecipeDetailedAction
    data class ToggleCartIngredient(val cartKey: String): RecipeDetailedAction
    data object ConfirmAddSelectedIngredientsToCart: RecipeDetailedAction
    data object ConsumeAddToCartResult: RecipeDetailedAction

    data class OnSheetStateChanged(val isExpanded: Boolean) : RecipeDetailedAction
}
