package bob.colbaskin.cookly.profile.presentation.profile


sealed interface ProfileAction {

    data object Refresh: ProfileAction
    data object Logout: ProfileAction
    data object ConsumeLogoutSuccess: ProfileAction
    data object DismissError: ProfileAction
    data object OpenApplicationsReview: ProfileAction
    data object OpenCookingHistory: ProfileAction
    data object OpenPreferencesAndAllergies: ProfileAction
    data object OpenRecipeStatuses: ProfileAction
    data object OpenCreateRecipe: ProfileAction
}
