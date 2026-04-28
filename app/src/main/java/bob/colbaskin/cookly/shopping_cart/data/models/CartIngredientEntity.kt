package bob.colbaskin.cookly.shopping_cart.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_ingredients")
data class CartIngredientEntity(
    @PrimaryKey val cartKey: String,
    val ingredientId: Int?,
    val title: String,
    val quantity: Double,
    val unitMeasurement: String,
    val sourceRecipeId: Int?,
    val createdAtMillis: Long
)
