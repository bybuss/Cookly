package bob.colbaskin.cookly.home.presentation.cook_steps

sealed interface CookStepsEvent {
    data object NavigateBack: CookStepsEvent
    data object NavigateHome: CookStepsEvent
}
