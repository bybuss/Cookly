package bob.colbaskin.cookly.create_recipe.data.models

import kotlinx.serialization.Serializable

@Serializable
data class RecipeCategoryDto(
    val id: Int,
    val title: String
)
