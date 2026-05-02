package bob.colbaskin.cookly.home.presentation.main

import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.home.domain.models.main.ActiveCookingSession
import bob.colbaskin.cookly.home.domain.models.main.FeedRecipe
import bob.colbaskin.cookly.home.domain.models.meal.MealTimeType
import bob.colbaskin.cookly.home.domain.models.main.QuickCategoryType

data class HomeState(
    val quickCardsList: List<QuickCategoryType> = listOf(
        QuickCategoryType.QUICK_COOK,
        QuickCategoryType.DIETARY,
        QuickCategoryType.HIGH_CALORIE,
        QuickCategoryType.ON_TREND,
    ),
    val mealsList: List<MealTimeType> = listOf(
        MealTimeType.BREAKFAST,
        MealTimeType.LUNCH,
        MealTimeType.SUPPER,
    ),
    val feedState: UiState<List<FeedRecipe>> = UiState.Idle,
    val appendState: UiState<Unit> = UiState.Idle,
    val recipes: List<FeedRecipe> = emptyList(),
    val lastRecipeScore: Double? = null,
    val lastRecipeId: Int? = null,
    val paginationKey: String? = null,
    val isEndReached: Boolean = false,

    val activeCookingSessions: UiState<List<ActiveCookingSession>?> = UiState.Idle
)
