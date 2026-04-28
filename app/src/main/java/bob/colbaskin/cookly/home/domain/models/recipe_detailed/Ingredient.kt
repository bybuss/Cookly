package bob.colbaskin.cookly.home.domain.models.recipe_detailed

data class Ingredient(
    val name: String,
    val count: Int,
    val unitOfMeasurement: String
)
