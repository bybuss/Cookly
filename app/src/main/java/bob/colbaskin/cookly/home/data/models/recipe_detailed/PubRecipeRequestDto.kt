package bob.colbaskin.cookly.home.data.models.recipe_detailed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PubRecipeRequestDto(
    val id: Int,
    val feedback: String? = null,
    val status: String,
    @SerialName("reviewed_at") val reviewedAt: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("recipe_id") val recipeId: Int,

)
