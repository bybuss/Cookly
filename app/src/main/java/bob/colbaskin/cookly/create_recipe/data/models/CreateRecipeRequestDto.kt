package bob.colbaskin.cookly.create_recipe.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRecipeRequestDto(
    val title: String,
    val description: String,
    @SerialName("estimated_time") val estimatedTime: Int,
    @SerialName("calories_by_100grams") val caloriesBy100Grams: Int? = null,
    @SerialName("meal_time") val mealTime: String? = null,
    val steps: List<CreateRecipeStepDto>,
    @SerialName("recipe_ingredients") val recipeIngredients: List<CreateRecipeIngredientDto>,
    @SerialName("recipe_categories_ids") val recipeCategoriesIds: List<Int>
)
