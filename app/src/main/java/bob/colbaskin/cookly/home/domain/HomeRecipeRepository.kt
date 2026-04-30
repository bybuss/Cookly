package bob.colbaskin.cookly.home.domain

import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.home.domain.models.main.FeedPage
import bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeDetailed

interface HomeRecipeRepository {
    suspend fun getRecipeById(recipeId: Int): ApiResult<RecipeDetailed>

    suspend fun getUserFeed(
        lastScore: Double?,
        lastId: Int?,
        paginationKey: String?,
        limit: Int
    ): ApiResult<FeedPage>
}
