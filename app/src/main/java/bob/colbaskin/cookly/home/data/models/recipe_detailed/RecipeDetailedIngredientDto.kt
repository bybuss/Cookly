package bob.colbaskin.cookly.home.data.models.recipe_detailed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDetailedIngredientDto(
    val id: Int,
    val title: String,
    @SerialName("default_unit_measurement")
    val defaultUnitMeasurement: String,
    @SerialName("created_at")
    val createdAt: String
)
