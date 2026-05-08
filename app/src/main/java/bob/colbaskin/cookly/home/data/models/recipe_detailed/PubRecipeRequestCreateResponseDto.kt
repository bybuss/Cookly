package bob.colbaskin.cookly.home.data.models.recipe_detailed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PubRecipeRequestCreateResponseDto(
    @SerialName("pub_recipe_request_id")
    val pubRecipeRequestId: Int
)
