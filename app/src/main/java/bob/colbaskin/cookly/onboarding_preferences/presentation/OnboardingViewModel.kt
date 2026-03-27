package bob.colbaskin.cookly.onboarding_preferences.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.common.user_prefs.data.models.OnboardingConfig
import bob.colbaskin.cookly.common.user_prefs.domain.UserPreferencesRepository
import bob.colbaskin.cookly.onboarding_preferences.domain.models.AllergyOption
import bob.colbaskin.cookly.onboarding_preferences.domain.models.DietOption
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

private const val TAG = "OnboardingVM"

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    var state by mutableStateOf(OnboardingState())
        private set

    private val _effect = MutableSharedFlow<OnboardingEffect>()
    val effect = _effect

    fun onAction(action: OnboardingAction) {
        when (action) {
            is OnboardingAction.ChangePage -> changePage(action.pageIndex)
            is OnboardingAction.ToggleDiet -> toggleDiet(action.diet)
            is OnboardingAction.ToggleAllergy -> toggleAllergy(action.allergy)
            OnboardingAction.NextPage -> nextPage()
            OnboardingAction.Skip -> skip()
            OnboardingAction.Finish -> finish()
        }
    }

    private fun toggleDiet(diet: DietOption) {
        val newSet = state.selectedDiets.toMutableSet()
        if (!newSet.add(diet)) newSet.remove(diet)
        state = state.copy(selectedDiets = newSet)
        Log.d(TAG, "Toggled diet: ${diet.name}, new set: ${newSet.map { it.name }}")
    }

    private fun toggleAllergy(allergy: AllergyOption) {
        val newSet = state.selectedAllergies.toMutableSet()
        if (!newSet.add(allergy)) newSet.remove(allergy)
        state = state.copy(selectedAllergies = newSet)
        Log.d(TAG, "Toggled allergy: ${allergy.name}, new set: ${newSet.map { it.name }}")
    }

    private fun changePage(pageIndex: Int) {
        state = state.copy(currentPageIndex = pageIndex)
    }

    private fun nextPage() {
        viewModelScope.launch {
            val nextPage = state.currentPageIndex + 1
            state = state.copy(currentPageIndex = nextPage)
            _effect.emit(OnboardingEffect.ScrollToPage(nextPage))
        }
    }

    private fun finish() {
        viewModelScope.launch {
            userPreferencesRepository.saveOnboardingStatus(OnboardingConfig.COMPLETED)
            _effect.emit(OnboardingEffect.CompleteOnboarding)
        }
    }

    private fun skip() {
        viewModelScope.launch {
            userPreferencesRepository.saveOnboardingStatus(OnboardingConfig.COMPLETED)
            _effect.emit(OnboardingEffect.CompleteOnboarding)
        }
    }
}
