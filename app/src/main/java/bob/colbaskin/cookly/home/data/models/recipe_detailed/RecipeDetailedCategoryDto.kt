package bob.colbaskin.cookly.home.data.models.recipe_detailed

import kotlinx.serialization.Serializable

@Serializable
data class RecipeDetailedCategoryDto(
    val id: Int,
    val title: String
)
