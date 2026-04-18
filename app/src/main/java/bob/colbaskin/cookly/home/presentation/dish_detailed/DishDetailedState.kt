package bob.colbaskin.cookly.home.presentation.dish_detailed

import androidx.annotation.DrawableRes
import bob.colbaskin.cookly.R

data class DishDetailedState(
    val mealType: String = "Завтраки",
    val dishName: String = "Яичница\nМятые Иички",
    @DrawableRes val dishAvatar: Int = R.drawable.fried_egg_backgroiund, // FIXME: потом будет url и через AsyncImage нужно будет
    val rating: Double = 4.8,
    val ratingAmount: Int = 163,
    val minutes: Int = 20,
    val kcal: Int = 150,
    val isRecipeLiked: Boolean = true,
    val isFlameIconRed: Boolean = false,
    val isSheetExpanded: Boolean = false
)
