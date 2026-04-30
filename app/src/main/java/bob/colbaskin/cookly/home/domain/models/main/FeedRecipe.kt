package bob.colbaskin.cookly.home.domain.models.main

data class FeedRecipe(
    val id: Int,
    val title: String,
    val estimatedTime: Int,
    val caloriesBy100Grams: Double,
    val mealTime: String,
    val ratingSum: Double,
    val ratingCount: Int,
    val spicyLevel: Int,
    val difficultyLevel: Int,
    val imageUrl: String
) {
    val rating: Double
        get() = if (ratingCount == 0) 0.0 else ratingSum / ratingCount

    val isFlameIconRed: Boolean
        get() = caloriesBy100Grams >= 300
}
