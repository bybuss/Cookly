package bob.colbaskin.cookly.common.recipe_preview.data.models

import kotlinx.serialization.Serializable

@Serializable
data class RecipesResponseDto(
    val recipes: List<RecipePreviewDto>
)
