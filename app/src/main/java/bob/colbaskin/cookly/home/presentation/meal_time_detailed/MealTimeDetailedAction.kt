package bob.colbaskin.cookly.home.presentation.meal_time_detailed

interface MealTimeDetailedAction {
    data object NavigateBack: MealTimeDetailedAction
    data class NavigateToMealRecipe(val id: Int): MealTimeDetailedAction
    data class OnPagerPageSettled(val page: Int) : MealTimeDetailedAction
    data class OnSheetStateChanged(val isExpanded: Boolean) : MealTimeDetailedAction
}
