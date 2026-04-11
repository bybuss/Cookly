package bob.colbaskin.cookly.home.presentation.meal_detailed

interface MealDetailedAction {
    data object NavigateBack: MealDetailedAction
    data class NavigateToMealRecipe(val id: Int): MealDetailedAction

}
