package bob.colbaskin.cookly.create_recipe.domain.models

data class CreateRecipeStep(
    val localId: Long,
    val number: Int,
    val title: String = "",
    val description: String = "",
    val image: LocalImage? = null
)
