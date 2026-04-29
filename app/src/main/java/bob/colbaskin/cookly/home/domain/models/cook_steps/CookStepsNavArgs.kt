package bob.colbaskin.cookly.home.domain.models.cook_steps

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

const val COOK_STEPS_ARGS_KEY = "cook_steps_args"

@Parcelize
data class CookStepsNavArgs(
    val recipeId: Int,
    val recipeTitle: String,
    val mealType: String,
    val recipeImageUrl: String?,
    val steps: List<CookStep>
): Parcelable
