package bob.colbaskin.cookly.create_recipe.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeStepResponseDto(
    val id: Int,
    val title: String,
    val description: String,
    val number: Int,
    @SerialName("image_url") val imageUrl: String? = null
)
