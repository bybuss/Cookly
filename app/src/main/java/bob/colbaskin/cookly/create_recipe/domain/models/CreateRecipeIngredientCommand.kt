package bob.colbaskin.cookly.create_recipe.domain.models

data class CreateRecipeIngredientCommand(
    val ingredientId: Int,
    val quantity: Double,
    val unitMeasurement: String
)
