package bob.colbaskin.cookly.home.domain.models.main

import androidx.annotation.StringRes
import bob.colbaskin.cookly.R

enum class QuickCategoryType(
    @param:StringRes val textId: Int
) {
    QUICK_COOK(textId = R.string.quick_category_quick_cook),
    DIETARY(textId = R.string.quick_category_dietary),
    HIGH_CALORIE(textId = R.string.quick_category_high_calorie),
    ON_TREND(textId = R.string.quick_category_on_trend),
}