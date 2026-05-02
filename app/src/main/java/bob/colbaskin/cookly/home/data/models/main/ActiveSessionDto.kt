package bob.colbaskin.cookly.home.data.models.main

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActiveSessionDto(
    val session: SessionDto,
    @SerialName("recipe_id") val recipeId: Int,
    @SerialName("recipe_title") val recipeTitle: String,
    @SerialName("step_title") val stepTitle: String,
    @SerialName("recipe_image_url") val recipeImageUrl: String,
)
