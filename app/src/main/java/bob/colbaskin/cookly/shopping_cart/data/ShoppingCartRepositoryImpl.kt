package bob.colbaskin.cookly.shopping_cart.data

import android.util.Log
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.shopping_cart.data.models.toDomain
import bob.colbaskin.cookly.shopping_cart.data.models.toEntity
import bob.colbaskin.cookly.shopping_cart.domain.ShoppingCartRepository
import bob.colbaskin.cookly.shopping_cart.domain.models.CartIngredient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "ShoppingCartRepository"

class ShoppingCartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : ShoppingCartRepository {

    override fun observeCartIngredients(): Flow<List<CartIngredient>> {
        return cartDao.observeCartIngredients()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun addIngredients(ingredients: List<CartIngredient>): ApiResult<Unit> {
        return try {
            ingredients.forEach { ingredient ->
                val existing = cartDao.getByCartKey(ingredient.cartKey)
                if (existing == null) {
                    cartDao.upsert(ingredient.toEntity())
                } else {
                    cartDao.updateQuantity(
                        cartKey = ingredient.cartKey,
                        quantity = existing.quantity + ingredient.quantity
                    )
                }
            }
            ApiResult.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to add ingredients to cart", e)
            ApiResult.Error(
                title = "Не удалось добавить ингредиенты",
                text = e.message ?: "Ошибка локальной базы данных."
            )
        }
    }

    override suspend fun updateIngredientQuantity(
        cartKey: String,
        quantity: Double
    ): ApiResult<Unit> {
        return try {
            if (quantity <= 0.0) {
                cartDao.deleteByCartKey(cartKey)
            } else {
                cartDao.updateQuantity(
                    cartKey = cartKey,
                    quantity = quantity
                )
            }
            ApiResult.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update cart ingredient", e)
            ApiResult.Error(
                title = "Не удалось обновить ингредиент",
                text = e.message ?: "Ошибка локальной базы данных."
            )
        }
    }

    override suspend fun deleteIngredient(cartKey: String): ApiResult<Unit> {
        return try {
            cartDao.deleteByCartKey(cartKey)
            ApiResult.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete cart ingredient", e)
            ApiResult.Error(
                title = "Не удалось удалить ингредиент",
                text = e.message ?: "Ошибка локальной базы данных."
            )
        }
    }

    override suspend fun clearCart(): ApiResult<Unit> {
        return try {
            cartDao.clearCart()
            ApiResult.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to clear cart", e)
            ApiResult.Error(
                title = "Не удалось очистить корзину",
                text = e.message ?: "Ошибка локальной базы данных."
            )
        }
    }
}
