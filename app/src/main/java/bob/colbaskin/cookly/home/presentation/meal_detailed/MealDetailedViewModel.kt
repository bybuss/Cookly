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
            else -> Unit
        }
    }
}