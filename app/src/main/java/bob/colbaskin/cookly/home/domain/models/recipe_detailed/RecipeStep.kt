package bob.colbaskin.cookly.home.domain.models.recipe_detailed

data class RecipeStep(
    val id: Int,
    val title: String,
    val description: String,
    val number: Int,
    val imageUrl: String?
)
