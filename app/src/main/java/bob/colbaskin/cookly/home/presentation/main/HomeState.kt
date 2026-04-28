package bob.colbaskin.cookly.home.presentation.main

import bob.colbaskin.cookly.home.domain.models.old.MealType
import bob.colbaskin.cookly.home.domain.models.old.QuickCategoryType

data class HomeState(
    val quickCardsList: List<QuickCategoryType> = listOf(
        QuickCategoryType.QUICK_COOK,
        QuickCategoryType.DIETARY,
        QuickCategoryType.HIGH_CALORIE,
        QuickCategoryType.ON_TREND,
    ),
    val mealsList: List<MealType> = listOf(
        MealType.BREAKFAST,
        MealType.LUNCH,
        MealType.DINNER,
        MealType.DINNER,
    )
)
