package bob.colbaskin.cookly.chat.data.models

import bob.colbaskin.cookly.common.recipe_preview.data.models.RecipePreviewDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatResponseDto(
    @SerialName("is_food_related") val isFoodRelated: Boolean,
    @SerialName("text_response") val textResponse: String,
    val recipes: List<RecipePreviewDto>
)
