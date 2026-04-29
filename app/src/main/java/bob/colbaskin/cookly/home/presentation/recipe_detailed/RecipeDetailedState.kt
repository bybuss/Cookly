package bob.colbaskin.cookly.home.presentation.recipe_detailed

import androidx.annotation.DrawableRes
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.utils.getFirstLetter
import bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeDetailed

const val DEFAULT_RECIPE_PORTIONS = 1

data class RecipeDetailedState(
    val email: String = "",
    val avatarUrl: String = "",

    val recipeState: UiState<RecipeDetailed> = UiState.Idle,
    val isRecipeLiked: Boolean = false,
    val isSheetExpanded: Boolean = false,
    @param:DrawableRes val dishAvatarFallback: Int = R.drawable.fallback_avatar,

    val isAddToCartSheetVisible: Boolean = false,
    val portions: Int = DEFAULT_RECIPE_PORTIONS,
    val cartIngredients: List<RecipeCartIngredientUi> = emptyList(),
    val addToCartState: UiState<Unit> = UiState.Idle
) {
    val avatarLetter: String
        get() = email.getFirstLetter()
}
