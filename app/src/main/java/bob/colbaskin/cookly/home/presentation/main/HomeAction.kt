package bob.colbaskin.cookly.home.presentation.main

interface HomeAction {
    data object LoadInitialFeed : HomeAction
    data object RefreshFeed : HomeAction
    data object LoadNextFeedPage : HomeAction
    data class OpenRecipe(val recipeId: Int) : HomeAction
    data class OpenMealTimeDetailed(val mealTimeType: String): HomeAction

    data class CancelActiveSession(val sessionId: Int): HomeAction

}
