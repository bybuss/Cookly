package bob.colbaskin.cookly.home.data.models.recipe_detailed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDetailedResponseDto(
    val recipe: RecipeDetailedDto,
    @SerialName("is_favorite") val isFavorite: Boolean,
    @SerialName("user_rate") val userRate: Int? = null,
)
