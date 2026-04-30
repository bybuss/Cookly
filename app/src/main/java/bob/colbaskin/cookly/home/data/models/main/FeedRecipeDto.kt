package bob.colbaskin.cookly.home.data.models.main

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedRecipeDto(
    val id: Int,
    val title: String,
    @SerialName("estimated_time") val estimatedTime: Int,
    @SerialName("calories_by_100grams") val caloriesBy100Grams: Double,
    @SerialName("meal_time") val mealTime: String,
    @SerialName("rating_sum") val ratingSum: Double = 0.0,
    @SerialName("rating_count") val ratingCount: Int = 0,
    @SerialName("spicy_level") val spicyLevel: Int = 0,
    @SerialName("difficulty_level") val difficultyLevel: Int = 1,
    @SerialName("image_url") val imageUrl: String
)
