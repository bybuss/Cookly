package bob.colbaskin.cookly.onboarding_preferences.presentation

import bob.colbaskin.cookly.onboarding_preferences.domain.models.AllergyOption
import bob.colbaskin.cookly.onboarding_preferences.domain.models.DietOption

data class OnboardingState(
    val currentPageIndex: Int = 0,
    val selectedDiets: Set<DietOption> = emptySet(), // FIXME: убрать диеты, оставив только выбор конкретных аллергенов
    val selectedAllergies: Set<AllergyOption> = emptySet() // FIXME: заменить на data class Allergen
)
