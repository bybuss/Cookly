package bob.colbaskin.cookly.common.recipe_preview.domain.models

data class RecipePreview(
    val id: Int,
    val title: String,
    val estimatedTime: Int,
    val caloriesBy100Grams: Double,
    val mealTime: MealTimeType,
    val ratingSum: Double,
    val ratingCount: Int,
    val spicyLevel: Int,
    val difficultyLevel: Int,
    val imageUrl: String
) {
    val averageRating: Double
        get() = if (ratingCount == 0) 0.0 else ratingSum / ratingCount.toDouble()
}
