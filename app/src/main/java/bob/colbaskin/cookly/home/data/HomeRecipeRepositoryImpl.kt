package bob.colbaskin.cookly.home.data

import android.content.Context
import android.util.Log
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.utils.safeApiCall
import bob.colbaskin.cookly.home.data.models.main.FeedResponseDto
import bob.colbaskin.cookly.home.data.models.main.toDomain
import bob.colbaskin.cookly.home.data.models.recipe_detailed.RecipeDetailedDto
import bob.colbaskin.cookly.home.domain.HomeRecipeRepository
import bob.colbaskin.cookly.home.domain.models.main.FeedPage
import bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeDetailed
import bob.colbaskin.cookly.home.domain.models.recipe_detailed.toDomain
import javax.inject.Inject


private const val TAG = "HomeRecipeRepository"

class HomeRecipeRepositoryImpl @Inject constructor(
    private val context: Context,
    private val apiService: HomeRecipeApiService
) : HomeRecipeRepository {

    override suspend fun getRecipeById(recipeId: Int): ApiResult<RecipeDetailed> {
        Log.d(TAG, "Get recipe by id=$recipeId")

        return safeApiCall<RecipeDetailedDto, RecipeDetailed>(
            apiCall = { apiService.getRecipeById(recipeId) },
            successHandler = { response -> response.toDomain() },
            context = context
        )
    }

    override suspend fun getUserFeed(
        lastScore: Double?,
        lastId: Int?,
        paginationKey: String?,
        limit: Int
    ): ApiResult<FeedPage> {
        Log.d(
            TAG,
            "Get user feed. lastScore=$lastScore, lastId=$lastId, paginationKey=$paginationKey, limit=$limit"
        )

        return safeApiCall<FeedResponseDto, FeedPage>(
            apiCall = {
                apiService.getUserFeed(
                    lastScore = lastScore,
                    lastId = lastId,
                    paginationKey = paginationKey,
                    limit = limit
                )
            },
            successHandler = { response -> response.toDomain() },
            context = context
        )
    }
}
