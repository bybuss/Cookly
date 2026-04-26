package bob.colbaskin.cookly.create_recipe.data.models

import kotlinx.serialization.Serializable

@Serializable
data class RecipeResponseDto(
    val id: Int,
    val title: String,
    val description: String,
    val steps: List<RecipeStepResponseDto> = emptyList()
)
