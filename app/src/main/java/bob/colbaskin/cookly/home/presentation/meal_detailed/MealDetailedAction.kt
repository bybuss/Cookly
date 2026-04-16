package bob.colbaskin.cookly.home.presentation.meal_detailed

interface MealDetailedAction {
    data object NavigateBack: MealDetailedAction
    data class NavigateToMealRecipe(val id: Int): MealDetailedAction
    data class OnPagerPageSettled(val page: Int) : MealDetailedAction
    data class OnSheetStateChanged(val isExpanded: Boolean) : MealDetailedAction
}
