package bob.colbaskin.cookly.home.presentation.recipe_detailed

import androidx.annotation.DrawableRes
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.home.domain.models.recipe_detailed.Ingredient
import bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeDetailed

data class RecipeDetailedState(

    val recipeState: UiState<RecipeDetailed> = UiState.Idle,
    val isRecipeLiked: Boolean = false,
    val isSheetExpanded: Boolean = false,
    @DrawableRes val dishAvatarFallback: Int = R.drawable.fried_egg_backgroiund,
)
