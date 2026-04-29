package bob.colbaskin.cookly.home.presentation.cook_steps

import androidx.annotation.DrawableRes
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.home.domain.models.cook_steps.CookStep
import bob.colbaskin.cookly.home.domain.models.cook_steps.CookStepsNavArgs
import kotlin.collections.orEmpty

data class CookStepsState(
    val args: CookStepsNavArgs? = null,
    val currentStepIndex: Int = 0,
    val isRatingSheetVisible: Boolean = false,
    val rating: Int = 0,
    val reviewText: String = "",
    @param:DrawableRes val fallbackImageRes: Int = R.drawable.fallback_avatar
) {
    val steps: List<CookStep>
        get() = args?.steps.orEmpty()

    val currentStep: CookStep?
        get() = steps.getOrNull(currentStepIndex)

    val currentStepNumber: Int
        get() = currentStepIndex + 1

    val isFirstStep: Boolean
        get() = currentStepIndex == 0

    val isLastStep: Boolean
        get() = currentStepIndex == steps.lastIndex

    val recipeTitle: String
        get() = args?.recipeTitle.orEmpty()

    val mealType: String
        get() = args?.mealType.orEmpty()

    val recipeImageUrl: String?
        get() = args?.recipeImageUrl
}
