package bob.colbaskin.cookly.onboarding_preferences.domain.models

import bob.colbaskin.cookly.R

enum class DietOption(val displayName: String, val imageRes: Int) {
    Vegan("Веган", R.drawable.smoothie_background),
    Meat("Мясоед", R.drawable.smoothie_background),
    Vegetarian("Вегетарианец", R.drawable.smoothie_background)
}
