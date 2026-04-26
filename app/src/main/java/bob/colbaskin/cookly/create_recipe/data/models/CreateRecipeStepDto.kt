package bob.colbaskin.cookly.create_recipe.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateRecipeStepDto(
    val title: String,
    val description: String,
    val number: Int
)
