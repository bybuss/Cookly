package bob.colbaskin.cookly.home.data.models.main

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionDto(
    val id: Int,
    @SerialName("recipe_id") val recipeId: Int,
    @SerialName("current_step") val currentStep: Int,
)
