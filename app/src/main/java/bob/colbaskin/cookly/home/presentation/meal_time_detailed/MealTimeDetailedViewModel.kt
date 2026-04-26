package bob.colbaskin.cookly.home.presentation.meal_detailed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class MealCategoryDetailedViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(MealDetailedState())
        private set

    fun onAction(action: MealCategoryDetailedAction) {
        when (action) {
            is MealCategoryDetailedAction.OnPagerPageSettled -> {
                state = state.copy(currentPage = action.page)
            }
            is MealCategoryDetailedAction.OnSheetStateChanged -> {
                state = state.copy(
                    isSheetExpanded = action.isExpanded,
                    isAutoScrollEnabled = !action.isExpanded
                )
            }
            else -> Unit
        }
    }
}