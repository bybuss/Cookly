package bob.colbaskin.cookly.home.presentation.main

import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.components.feed_pagination.PaginationState
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

    val feedPagination: PaginationState<FeedRecipe> = PaginationState(),

    val activeCookingSessions: UiState<List<ActiveCookingSession>?> = UiState.Idle
)
