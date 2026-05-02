package bob.colbaskin.cookly.home.presentation.meal_time_detailed

interface MealTimeDetailedAction {
    data object NavigateBack: MealTimeDetailedAction
    data class NavigateToRecipeDetailed(val recipeId: Int): MealTimeDetailedAction
    data class OnPagerPageSettled(val page: Int) : MealTimeDetailedAction
    data class OnSheetStateChanged(val isExpanded: Boolean) : MealTimeDetailedAction
    data object LoadNextPage: MealTimeDetailedAction
    data object Refresh: MealTimeDetailedAction
}
