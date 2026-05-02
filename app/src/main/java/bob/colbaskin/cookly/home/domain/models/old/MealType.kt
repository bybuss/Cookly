package bob.colbaskin.cookly.home.domain.models.old

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import bob.colbaskin.cookly.R

enum class MealType(
    @param:StringRes val displayNameId: Int,
    @param:DrawableRes val logoId: Int
) {
    BREAKFAST(
        displayNameId = R.string.breakfast_card_text,
        logoId = R.drawable.breakfast_logo
    ),
    LUNCH(
        displayNameId = R.string.lunch_card_text,
        logoId = R.drawable.lunch_logo
    ),
    AFTERNOON_SNACK(
        displayNameId = R.string.afternoon_snack_card_text,
        logoId = R.drawable.breakfast_logo // TODO: изменить лого на нужное
    ),
    DINNER(
        displayNameId = R.string.dinner_card_text,
        logoId = R.drawable.supper_logo
    ),
    SNACK(
        displayNameId = R.string.snack_card_text,
        logoId = R.drawable.breakfast_logo // TODO: изменить лого на нужное
    )
}
