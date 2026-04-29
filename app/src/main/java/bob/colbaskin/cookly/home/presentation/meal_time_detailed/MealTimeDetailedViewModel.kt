package bob.colbaskin.cookly.home.presentation.meal_time_detailed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.profile.domain.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class MealTimeDetailedViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {
    var state by mutableStateOf(MealDetailedState())
        private set

    init {
        observeLocalUser()
    }

    fun onAction(action: MealTimeDetailedAction) {
        when (action) {
            is MealTimeDetailedAction.OnPagerPageSettled -> {
                state = state.copy(currentPage = action.page)
            }
            is MealTimeDetailedAction.OnSheetStateChanged -> {
                state = state.copy(
                    isSheetExpanded = action.isExpanded,
                    isAutoScrollEnabled = !action.isExpanded
                )
            }
            else -> Unit
        }
    }

    private fun observeLocalUser() {
        viewModelScope.launch {
            repository.observeUserPreferences().collectLatest { prefs ->
                state = state.copy(
                    email = prefs.email,
                    avatarUrl = prefs.avatarUrl
                )
            }
        }
    }
}