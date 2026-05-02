package bob.colbaskin.cookly.home.domain.models.main

data class ActiveCookingSession(
    val sessionId: Int,
    val recipeId: Int,
    val recipeTitle: String,
    val currentStepNumber: Int,
    val stepTitle: String,
    val recipeImageUrl: String
)
