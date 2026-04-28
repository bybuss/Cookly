package bob.colbaskin.cookly.shopping_cart.domain.models

data class CartIngredient(
    val cartKey: String,
    val ingredientId: Int?,
    val title: String,
    val quantity: Double,
    val unitMeasurement: String,
    val sourceRecipeId: Int?
)
