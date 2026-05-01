package bob.colbaskin.cookly.onboarding_preferences.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.user_prefs.domain.UserPreferencesRepository
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.OnboardingConfig
import bob.colbaskin.cookly.onboarding_preferences.domain.OnboardingPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val onboardingPreferencesRepository: OnboardingPreferencesRepository
) : ViewModel() {

    var state by mutableStateOf(OnboardingState())
        private set

    private val _effect = MutableSharedFlow<OnboardingEffect>()
    val effect = _effect

    init {
        loadIngredientGroups()
    }

    fun onAction(action: OnboardingAction) {
        when (action) {
            OnboardingAction.LoadIngredientGroups -> loadIngredientGroups()
            is OnboardingAction.ToggleIngredientGroup -> toggleIngredientGroup(action.id)
            OnboardingAction.Finish -> finish()
            OnboardingAction.Skip -> skip()
        }
    }

    private fun loadIngredientGroups() {
        viewModelScope.launch {
            state = state.copy(ingredientGroupsState = UiState.Loading)

            when (val result = onboardingPreferencesRepository.getIngredientGroups()) {
                is ApiResult.Success -> {
                    val groups = result.data

                    state = state.copy(
                        ingredientGroupsState = UiState.Success(groups),
                        selectedIngredientGroupIds = groups
                            .filter { it.excludedByUser }
                            .map { it.id }
                            .toSet()
                    )
                }

                is ApiResult.Error -> {
                    state = state.copy(
                        ingredientGroupsState = UiState.Error(
                            title = result.title,
                            text = result.text
                        )
                    )
                }
            }
        }
    }

    private fun toggleIngredientGroup(id: Int) {
        val newSet = state.selectedIngredientGroupIds.toMutableSet()
        if (!newSet.add(id)) { newSet.remove(id) }
        state = state.copy(selectedIngredientGroupIds = newSet)
    }

    private fun finish() {
        viewModelScope.launch {
            state = state.copy(isSaving = true)
            val result = onboardingPreferencesRepository.setExcludeIngredientGroups(
                ingredientGroupIds = state.selectedIngredientGroupIds.toList()
            )
            state = state.copy(isSaving = false)
            when (result) {
                is ApiResult.Success -> {
                    userPreferencesRepository.saveOnboardingStatus(OnboardingConfig.COMPLETED)
                    _effect.emit(OnboardingEffect.CompleteOnboarding)
                }
                is ApiResult.Error -> {
                    state = state.copy(
                        ingredientGroupsState = UiState.Error(
                            title = result.title,
                            text = result.text
                        )
                    )
                }
            }
        }
    }

    private fun skip() {
        viewModelScope.launch {
            userPreferencesRepository.saveOnboardingStatus(OnboardingConfig.COMPLETED)
            _effect.emit(OnboardingEffect.CompleteOnboarding)
        }
    }
}
