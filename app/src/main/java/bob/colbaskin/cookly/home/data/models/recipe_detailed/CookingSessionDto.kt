package bob.colbaskin.cookly.home.data.models.recipe_detailed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CookingSessionDto(
    @SerialName("cooking_session_id") val cooingSessionId: Int,
)
