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
    data class SearchResult(
        val query: String = "",
        val includeIngredientGroupIds: List<Int> = emptyList(),
        val maxSpicy: Int? = null,
        val maxDifficulty: Int? = null,
        val maxCaloriesBy100Grams: Double? = null,
        val mealTimeType: String? = null,
        val minAvgRating: Double? = null,
        val maxEstimatedCookingTime: Int? = null
    ): Screens

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
    data class MealTimeDetailed(val mealTimeType: String)

    @Serializable
    data class RecipeDetailed(val recipeId: Int)

    @Serializable
    data object CookSteps

    @Serializable
    data object OnModeration

    @Serializable
    data object CookingHistory

    @Serializable
    data object RecipeStatuses

    @Serializable
    data object CreateRecipe
}
