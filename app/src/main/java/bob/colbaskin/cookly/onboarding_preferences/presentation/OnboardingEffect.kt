package bob.colbaskin.cookly.onboarding_preferences.presentation

sealed interface OnboardingEffect {
    data class ScrollToPage(val page: Int): OnboardingEffect
    object CompleteOnboarding: OnboardingEffect
}
