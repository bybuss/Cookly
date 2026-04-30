package bob.colbaskin.cookly.onboarding_preferences.presentation

sealed interface OnboardingEffect {
    data object CompleteOnboarding : OnboardingEffect
}
