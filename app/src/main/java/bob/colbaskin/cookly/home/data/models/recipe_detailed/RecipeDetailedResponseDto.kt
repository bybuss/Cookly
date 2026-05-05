package bob.colbaskin.cookly.home.data.models.recipe_detailed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDetailedResponseDto(
    val recipe: RecipeDetailedDto,
    @SerialName("pub_recipe_request") val pubRecipeRequest: PubRecipeRequestDto? = null,
    @SerialName("is_favorite") val isFavorite: Boolean,
    @SerialName("user_rate") val userRate: Int? = null,
    @SerialName("existed_cooking_session") val existedCookingSession: Boolean = false,
)
