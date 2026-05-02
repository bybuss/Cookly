package bob.colbaskin.cookly.common.recipe_preview.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipePreviewDto(
    val id: Int,
    val title: String,
    @SerialName("estimated_time") val estimatedTime: Int,
    @SerialName("calories_by_100grams") val caloriesBy100Grams: Double,
    @SerialName("meal_time") val mealTime: String,
    @SerialName("rating_sum") val ratingSum: Double,
    @SerialName("rating_count") val ratingCount: Int,
    @SerialName("spicy_level") val spicyLevel: Int,
    @SerialName("difficulty_level") val difficultyLevel: Int,
    @SerialName("image_url") val imageUrl: String
)
