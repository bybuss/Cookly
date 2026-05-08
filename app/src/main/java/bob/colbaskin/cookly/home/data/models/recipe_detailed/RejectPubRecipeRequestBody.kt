package bob.colbaskin.cookly.home.data.models.recipe_detailed

import kotlinx.serialization.Serializable

@Serializable
data class RejectPubRecipeRequestBody(
    val feedback: String
)
