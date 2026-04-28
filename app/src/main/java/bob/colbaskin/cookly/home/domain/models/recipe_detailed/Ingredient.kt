package bob.colbaskin.cookly.home.domain.models.recipe_detailed

data class Ingredient(
    val id: Int,
    val name: String,
    val count: Int,
    val unitOfMeasurement: String
)
