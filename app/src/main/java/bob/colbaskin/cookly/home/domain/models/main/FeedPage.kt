package bob.colbaskin.cookly.home.domain.models.main

data class FeedPage(
    val recipes: List<FeedRecipe>,
    val lastRecipeScore: Double?,
    val lastRecipeId: Int?,
    val paginationKey: String?
)
