package bob.colbaskin.cookly.home.data.models.recipe_detailed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDetailedIngredientItemDto(
    val ingredient: RecipeDetailedIngredientDto,
    @SerialName("unit_measurement")
    val unitMeasurement: String,
    val quantity: Double
)
