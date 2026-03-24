package bob.colbaskin.cookly.onboarding_preferences.presentation

import bob.colbaskin.cookly.onboarding_preferences.domain.models.AllergyOption
import bob.colbaskin.cookly.onboarding_preferences.domain.models.DietOption

sealed interface OnboardingAction {
    data class ToggleDiet(val diet: DietOption) : OnboardingAction
    data class ToggleAllergy(val allergy: AllergyOption) : OnboardingAction
    data class ChangePage(val pageIndex: Int) : OnboardingAction
    data object NextPage: OnboardingAction
    data object Finish: OnboardingAction
    data object Skip: OnboardingAction
}
