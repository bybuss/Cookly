package bob.colbaskin.cookly.home.presentation.recipe_detailed

sealed interface RecipeDetailedAction {
    data object NavigateBack: RecipeDetailedAction
    data object NavigateProfile: RecipeDetailedAction
    data object NavigateEditRecipe: RecipeDetailedAction

    data object StartCook: RecipeDetailedAction
    data class ChangeActiveStep(val cookingSessionId: Int): RecipeDetailedAction
    data object ConsumeStartCookingResult: RecipeDetailedAction
    data object ToggleLike: RecipeDetailedAction

    data object ShowAddToCartSheet: RecipeDetailedAction
    data object HideAddToCartSheet: RecipeDetailedAction
    data object IncreasePortions: RecipeDetailedAction
    data object DecreasePortions: RecipeDetailedAction
    data class ToggleCartIngredient(val cartKey: String): RecipeDetailedAction
    data object ConfirmAddSelectedIngredientsToCart: RecipeDetailedAction
    data object ConsumeAddToCartResult: RecipeDetailedAction

    data class OnSheetStateChanged(val isExpanded: Boolean): RecipeDetailedAction

    data object ShowModeratorReviewSheet: RecipeDetailedAction
    data object HideModeratorReviewSheet: RecipeDetailedAction

    data object RequestPublishRecipe: RecipeDetailedAction
    data object ApproveRecipe: RecipeDetailedAction
    data object WithdrawPublishRequest: RecipeDetailedAction

    data object ShowRejectRecipeSheet: RecipeDetailedAction
    data object HideRejectRecipeSheet: RecipeDetailedAction
    data class ChangeRejectFeedback(val feedback: String): RecipeDetailedAction
    data object RejectRecipe: RecipeDetailedAction

    data object DeleteRecipe: RecipeDetailedAction
    data object ShowDeleteRecipeDialog : RecipeDetailedAction
    data object HideDeleteRecipeDialog : RecipeDetailedAction

    data object ShowRatingSheet: RecipeDetailedAction
    data object HideRatingSheet: RecipeDetailedAction
    data class ChangeRating(val rating: Int): RecipeDetailedAction
    data object SubmitRating: RecipeDetailedAction
    data object ConsumeRatingResult: RecipeDetailedAction
}
