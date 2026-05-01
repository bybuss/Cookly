package bob.colbaskin.cookly.onboarding_preferences.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IngredientGroupDto(
    val id: Int,
    val title: String,
    @SerialName("excluded_by_user") val excludedByUser: Boolean
)