package bob.colbaskin.cookly.home.presentation.dish_detailed

interface DishDetailedAction {
    data object NavigateBack: DishDetailedAction
    data object StartCook: DishDetailedAction
    data object AddIngredientsToCart: DishDetailedAction
    data object ToggleLike: DishDetailedAction
    data object AddToCart: DishDetailedAction
    data class OnSheetStateChanged(val isExpanded: Boolean) : DishDetailedAction
}
