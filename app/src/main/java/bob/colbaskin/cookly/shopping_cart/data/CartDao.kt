package bob.colbaskin.cookly.shopping_cart.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import bob.colbaskin.cookly.shopping_cart.data.models.CartIngredientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("select * from cart_ingredients order by createdAtMillis desc")
    fun observeCartIngredients(): Flow<List<CartIngredientEntity>>

    @Query("select * from cart_ingredients where cartKey = :cartKey limit 1")
    suspend fun getByCartKey(cartKey: String): CartIngredientEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: CartIngredientEntity)

    @Query("update cart_ingredients set quantity = :quantity where cartKey = :cartKey")
    suspend fun updateQuantity(
        cartKey: String,
        quantity: Double
    )

    @Query("DELETE FROM cart_ingredients WHERE cartKey = :cartKey")
    suspend fun deleteByCartKey(cartKey: String)

    @Query("DELETE FROM cart_ingredients")
    suspend fun clearCart()
}
