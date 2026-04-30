package bob.colbaskin.cookly.onboarding_preferences.data.models

import kotlinx.serialization.Serializable

@Serializable
data class IngredientGroupDto(
    val id: Int,
    val title: String
)