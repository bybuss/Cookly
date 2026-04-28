package bob.colbaskin.cookly.home.data.models.recipe_detailed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDetailedDto(
    val id: Int,
    val title: String,
    val description: String,
    @SerialName("author_id") val authorId: String,
    @SerialName("estimated_time") val estimatedTime: Int,
    @SerialName("calories_by_100grams") val caloriesBy100Grams: Double,
    @SerialName("meal_time") val mealTime: String,
    @SerialName("rating_sum") val ratingSum: Double,
    @SerialName("rating_count") val ratingCount: Int,
    @SerialName("is_public") val isPublic: Boolean,
    @SerialName("spicy_level") val spicyLevel: Int,
    @SerialName("difficulty_level") val difficultyLevel: Int,
    @SerialName("created_at") val createdAt: String,
    val steps: List<RecipeDetailedStepDto> = emptyList(),
    @SerialName("recipe_ingredients")
    val recipeIngredients: List<RecipeDetailedIngredientItemDto> = emptyList(),
    @SerialName("recipe_categories")
    val recipeCategories: List<RecipeDetailedCategoryDto> = emptyList(),
    @SerialName("image_url") val imageUrl: String? = null
)
