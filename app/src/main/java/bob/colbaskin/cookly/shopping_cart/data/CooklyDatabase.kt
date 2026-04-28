package bob.colbaskin.cookly.shopping_cart.data

import androidx.room.Database
import androidx.room.RoomDatabase
import bob.colbaskin.cookly.shopping_cart.data.models.CartIngredientEntity

@Database(
    entities = [
        CartIngredientEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CooklyDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}
