package bob.colbaskin.cookly.home.data.models.main

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedResponseDto(
    val recipes: List<FeedRecipeDto> = emptyList(),
    @SerialName("last_recipe_score") val lastRecipeScore: Double? = null,
    @SerialName("last_recipe_id") val lastRecipeId: Int? = null,
    @SerialName("pagination_key") val paginationKey: String? = null
)
