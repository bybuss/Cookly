package bob.colbaskin.cookly.home.data.models.recipe_detailed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDetailedStepDto(
    val id: Int,
    val title: String,
    val description: String,
    val number: Int,
    @SerialName("image_url")
    val imageUrl: String? = null
)
