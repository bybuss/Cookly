package bob.colbaskin.cookly.search_result.domain.models

import bob.colbaskin.cookly.common.recipe_preview.domain.models.MealTimeType

data class RecipeSearchFilters(
    val query: String = "",
    val includeIngredientGroupIds: List<Int> = emptyList(),
    val maxSpicy: Int? = null,
    val maxDifficulty: Int? = null,
    val maxCaloriesBy100Grams: Double? = null,
    val mealTimeType: MealTimeType? = null,
    val minAvgRating: Double? = null,
    val maxEstimatedCookingTime: Int? = null
) {
    val isEmpty: Boolean
        get() =
            query.isBlank() &&
                    includeIngredientGroupIds.isEmpty() &&
                    maxSpicy == null &&
                    maxDifficulty == null &&
                    maxCaloriesBy100Grams == null &&
                    mealTimeType == null &&
                    minAvgRating == null &&
                    maxEstimatedCookingTime == null
}
