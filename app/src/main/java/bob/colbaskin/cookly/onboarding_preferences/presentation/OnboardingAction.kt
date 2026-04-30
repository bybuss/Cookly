package bob.colbaskin.cookly.onboarding_preferences.presentation

sealed interface OnboardingAction {
    data object LoadIngredientGroups: OnboardingAction
    data class ToggleIngredientGroup(val id: Int): OnboardingAction
    data object Finish: OnboardingAction
    data object Skip: OnboardingAction
}
