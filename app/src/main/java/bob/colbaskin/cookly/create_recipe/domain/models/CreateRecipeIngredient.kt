package bob.colbaskin.cookly.create_recipe.domain.models

data class CreateRecipeIngredient(
    val ingredientId: Int,
    val title: String,
    val quantity: String = "",
    val unitMeasurement: String
)
