package bob.colbaskin.cookly.home.presentation.meal_detailed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class MealDetailedViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(MealDetailedState())
        private set

    fun onAction(action: MealDetailedAction) {
        when (action) {
            is MealDetailedAction.OnPagerPageSettled -> {
                state = state.copy(currentPage = action.page)
            }
            is MealDetailedAction.OnSheetStateChanged -> {
                state = state.copy(
                    isSheetExpanded = action.isExpanded,
                    isAutoScrollEnabled = !action.isExpanded
                )
            }
            else -> Unit
        }
    }
}