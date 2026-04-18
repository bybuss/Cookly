package bob.colbaskin.cookly.home.presentation.dish_detailed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class DishDetailedViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(DishDetailedState())
        private set

    fun onAction(action: DishDetailedAction) {
        when (action) {
            is DishDetailedAction.OnSheetStateChanged ->
                state = state.copy(isSheetExpanded = action.isExpanded)
            else -> Unit
        }
    }
}
