package bob.colbaskin.cookly.create_recipe.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRecipeIngredientDto(
    @SerialName("ingredient_id") val ingredientId: Int,
    @SerialName("unit_measurement") val unitMeasurement: String,
    val quantity: Double
)
