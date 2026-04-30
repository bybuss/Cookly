package bob.colbaskin.cookly.onboarding_preferences.presentation

import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.onboarding_preferences.domain.models.IngredientGroup

data class OnboardingState(
    val ingredientGroupsState: UiState<List<IngredientGroup>> = UiState.Idle,
    val selectedIngredientGroupIds: Set<Int> = emptySet(),
    val isSaving: Boolean = false
)
