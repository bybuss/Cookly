package bob.colbaskin.cookly.navigation

import kotlinx.serialization.Serializable

sealed interface Screens {

    @Serializable
    data object WebViewAuth: Screens

    @Serializable
    data object Preferences

    @Serializable
    data object Home: Screens

    @Serializable
    data object ShoppingCart: Screens

    @Serializable
    data object Chat: Screens

    @Serializable
    data object Favourites: Screens

    @Serializable
    data object Profile: Screens

    @Serializable
    data object Agreement: Screens

    @Serializable
    data object Policy: Screens

    @Serializable
    data object TermsOfUse: Screens

    @Serializable
    data object MealTimeDetailed

    @Serializable
    data class RecipeDetailed(val recipeId: Int)

    @Serializable
    data object CookSteps

    @Serializable
    data object ApplicationsReview

    @Serializable
    data object CookingHistory

    @Serializable
    data object RecipeStatuses

    @Serializable
    data object CreateRecipe
}
