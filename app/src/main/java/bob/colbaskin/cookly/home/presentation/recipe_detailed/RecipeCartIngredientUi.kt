package bob.colbaskin.cookly.home.presentation.recipe_detailed

data class RecipeCartIngredientUi(
    val cartKey: String,
    val ingredientId: Int?,
    val title: String,
    val baseQuantity: Double,
    val calculatedQuantity: Double,
    val unitMeasurement: String,
    val isSelected: Boolean
)
