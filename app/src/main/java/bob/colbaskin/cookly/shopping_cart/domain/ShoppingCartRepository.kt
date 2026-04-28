package bob.colbaskin.cookly.shopping_cart.domain

import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.shopping_cart.domain.models.CartIngredient
import kotlinx.coroutines.flow.Flow

interface ShoppingCartRepository {

    fun observeCartIngredients(): Flow<List<CartIngredient>>
    suspend fun addIngredients(ingredients: List<CartIngredient>): ApiResult<Unit>
    suspend fun updateIngredientQuantity(cartKey: String, quantity: Double): ApiResult<Unit>
    suspend fun deleteIngredient(cartKey: String): ApiResult<Unit>
    suspend fun clearCart(): ApiResult<Unit>
}
