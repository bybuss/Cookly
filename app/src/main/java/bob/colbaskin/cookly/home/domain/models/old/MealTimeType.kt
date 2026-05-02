package bob.colbaskin.cookly.home.domain.models.old

import androidx.annotation.DrawableRes
import bob.colbaskin.cookly.R

enum class MealTimeType(
    val apiValue: String,
    val title: String,
    @param:DrawableRes val logoId: Int
) {
    BREAKFAST(
        apiValue = "breakfast",
        title = "Завтрак",
        logoId = R.drawable.breakfast_logo
    ),
    LUNCH(
        apiValue = "lunch",
        title = "Обед",
        logoId = R.drawable.lunch_logo
    ),
    SUPPER(
        apiValue = "supper",
        title = "Ужин",
        logoId = R.drawable.supper_logo
    )
}
