package bob.colbaskin.cookly.home.presentation.recipe_detailed

import androidx.annotation.DrawableRes
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.user_prefs.domain.models.proto_configs.RoleConfig
import bob.colbaskin.cookly.common.utils.getFirstLetter
import bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeDetailed

const val DEFAULT_RECIPE_PORTIONS = 1

data class RecipeDetailedState(
    val id: Int = -1,
    val email: String = "",
    val avatarUrl: String = "",
    val role: RoleConfig = RoleConfig.USER,
    val isFavorite: Boolean = false,

    val userRate: Int? = null,

    val recipeState: UiState<RecipeDetailed> = UiState.Idle,
    val isSheetExpanded: Boolean = false,
    @param:DrawableRes val dishAvatarFallback: Int = R.drawable.fallback_avatar,

    val isAddToCartSheetVisible: Boolean = false,
    val portions: Int = DEFAULT_RECIPE_PORTIONS,
    val cartIngredients: List<RecipeCartIngredientUi> = emptyList(),
    val addToCartState: UiState<Unit> = UiState.Idle,

    val startCookingState: UiState<Int> = UiState.Idle,

    val isModeratorReviewSheetVisible: Boolean = false,

    val requestPublishState: UiState<Int> = UiState.Idle,
    val withdrawPublishRequestState: UiState<Unit> = UiState.Idle,
    val approveRecipeState: UiState<Unit> = UiState.Idle,
    val rejectRecipeState: UiState<Unit> = UiState.Idle,
    val isRejectRecipeSheetVisible: Boolean = false,
    val rejectFeedback: String = "",
    val deleteRecipeState: UiState<Unit> = UiState.Idle,
    val isDeleteRecipeDialogVisible: Boolean = false,

    val isRatingSheetVisible: Boolean = false,
    val selectedRating: Int = userRate ?: 0,
    val setRateState: UiState<Unit> = UiState.Idle,
) {
    val avatarLetter: String
        get() = email.getFirstLetter()
}
