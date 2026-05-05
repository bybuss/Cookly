package bob.colbaskin.cookly.home.presentation.cook_steps

import bob.colbaskin.cookly.home.domain.models.cook_steps.CookStepsNavArgs

sealed interface CookStepsAction {
    data class Init(val args: CookStepsNavArgs): CookStepsAction
    data object BackToRecipe: CookStepsAction
    data object BackToHome: CookStepsAction
    data object NextStep: CookStepsAction
    data object PreviousStep: CookStepsAction
    data object FinishCooking: CookStepsAction
    data class SetRating(val rating: Int): CookStepsAction
    data object SubmitRating: CookStepsAction
    data object DismissRating: CookStepsAction
}
