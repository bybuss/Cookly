package bob.colbaskin.cookly.home.presentation.meal_detailed

interface MealCategoryDetailedAction {
    data object NavigateBack: MealCategoryDetailedAction
    data class NavigateToMealRecipe(val id: Int): MealCategoryDetailedAction
    data class OnPagerPageSettled(val page: Int) : MealCategoryDetailedAction
    data class OnSheetStateChanged(val isExpanded: Boolean) : MealCategoryDetailedAction
}
