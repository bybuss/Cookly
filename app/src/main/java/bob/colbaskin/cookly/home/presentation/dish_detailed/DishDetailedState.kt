package bob.colbaskin.cookly.home.presentation.dish_detailed

import androidx.annotation.DrawableRes
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.home.domain.models.Allergen
import bob.colbaskin.cookly.home.domain.models.Ingredient

data class DishDetailedState(
    val mealType: String = "Завтраки",
    val dishName: String = "Яичница\nМятые Иички",
    val description: String = "Очень вкусная свежая иичница их мятых иичек с вилочкой и хлебушком сочным и перчиком ммм",
    @DrawableRes val dishAvatar: Int = R.drawable.fried_egg_backgroiund, // FIXME: потом будет url и через AsyncImage нужно будет
    val rating: Double = 4.8,
    val ratingAmount: Int = 163,
    val minutes: Int = 20,
    val kcal: Int = 150,
    val difficulty: String = "Легко",
    val spicyLvl: Int = 3,
    val allergensList: List<Allergen> = mockAllergensList,
    val isRecipeLiked: Boolean = true,
    val isFlameIconRed: Boolean = false,
    val isSheetExpanded: Boolean = false,
    val ingredientsList: List<Ingredient> = mockIngredientsList
)

private val mockIngredientsList = listOf(
    Ingredient(
        name = "Яйца",
        count = 2,
        unitOfMeasurement = "шт"
    ),
    Ingredient(
        name = "Молоко",
        count = 100,
        unitOfMeasurement = "мл"
    ),
    Ingredient(
        name = "Соль",
        count = 1,
        unitOfMeasurement = "щепотка"
    ),
    Ingredient(
        name = "Масло растительное",
        count = 1,
        unitOfMeasurement = "мл"
    )
)

private val mockAllergensList = listOf(
    Allergen(name = "Растительное масло"),
    Allergen(name = "Аллерген1"),
    Allergen(name = "Аллерген2"),
)
