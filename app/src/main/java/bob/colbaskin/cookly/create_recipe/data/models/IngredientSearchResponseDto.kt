package bob.colbaskin.cookly.create_recipe.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IngredientSearchResponseDto(
    val id: Int,
    val title: String,
    @SerialName("default_unit_measurement") val defaultUnitMeasurement: String,
    @SerialName("created_at") val createdAt: String? = null
)
