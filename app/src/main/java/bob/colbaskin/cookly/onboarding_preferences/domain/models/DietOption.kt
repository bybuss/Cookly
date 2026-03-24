package bob.colbaskin.cookly.onboarding_preferences.domain.models

import bob.colbaskin.cookly.R

enum class DietOption(val displayName: String, val imageRes: Int) {
    Vegetarian("Вегетарианская", R.drawable.smoothie_background),
    Ketogenic("Кето", R.drawable.smoothie_background),
    Paleo("Палео", R.drawable.smoothie_background),
    LowCarb("Низкоуглеводная", R.drawable.smoothie_background),
    LowFat("Низкожировая", R.drawable.smoothie_background),
    Balanced("Сбалансированная", R.drawable.smoothie_background),
    Mediterranean("Средиземноморская", R.drawable.smoothie_background),
    JapaneseCuisine("Японская", R.drawable.smoothie_background),
    ItalianCuisine("Итальянская", R.drawable.smoothie_background),
    MexicanCuisine("Мексиканская", R.drawable.smoothie_background),
    IndianCuisine("Индийская", R.drawable.smoothie_background),
    ThaiCuisine("Тайская", R.drawable.smoothie_background),
    FrenchCuisine("Французская", R.drawable.smoothie_background),
    ChineseCuisine("Китайская", R.drawable.smoothie_background)
}
