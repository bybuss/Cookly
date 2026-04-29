package bob.colbaskin.cookly.home.presentation.cook_steps

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class CookStepsViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(CookStepsState())
        private set

    private val eventChannel = Channel<CookStepsEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: CookStepsAction) {
        when (action) {
            is CookStepsAction.Init -> {
                if (state.args == null) {
                    state = state.copy(args = action.args)
                }
            }
            CookStepsAction.BackToRecipe -> {
                eventChannel.trySend(CookStepsEvent.NavigateBack)
            }
            CookStepsAction.BackToHome -> {
                eventChannel.trySend(CookStepsEvent.NavigateHome)
            }
            CookStepsAction.NextStep -> {
                state = if (state.isLastStep) {
                    state.copy(isRatingSheetVisible = true)
                } else {
                    state.copy(currentStepIndex = state.currentStepIndex + 1)
                }
            }
            CookStepsAction.PreviousStep -> {
                if (!state.isFirstStep) {
                    state = state.copy(currentStepIndex = state.currentStepIndex - 1)
                }
            }
            CookStepsAction.FinishCooking -> {
                state = state.copy(isRatingSheetVisible = true)
            }
            is CookStepsAction.UpdateRating -> {
                state = state.copy(rating = action.value.coerceIn(1, 5))
            }
            is CookStepsAction.UpdateReviewText -> {
                state = state.copy(
                    reviewText = action.value.take(300)
                )
            }
            CookStepsAction.SubmitRating -> {
                state = state.copy(isRatingSheetVisible = false)
                // TODO: потом добавить запрос на оценку рецепта еще
                eventChannel.trySend(CookStepsEvent.NavigateHome)
            }
            CookStepsAction.DismissRating -> {
                state = state.copy(isRatingSheetVisible = false)
                eventChannel.trySend(CookStepsEvent.NavigateHome)
            }
        }
    }
}
