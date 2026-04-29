package bob.colbaskin.cookly.home.presentation.meal_time_detailed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class MealTimeDetailedViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(MealDetailedState())
        private set

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
}