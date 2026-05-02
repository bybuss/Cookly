package bob.colbaskin.cookly.home.data

import android.content.Context
import android.util.Log
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.utils.safeApiCall
import bob.colbaskin.cookly.home.data.models.main.ActiveSessionDto
import bob.colbaskin.cookly.home.data.models.main.FeedResponseDto
import bob.colbaskin.cookly.home.data.models.main.toDomain
import bob.colbaskin.cookly.home.data.models.recipe_detailed.CookingSessionDto
import bob.colbaskin.cookly.home.data.models.recipe_detailed.RecipeDetailedResponseDto
import bob.colbaskin.cookly.home.domain.HomeRecipeRepository
import bob.colbaskin.cookly.home.domain.models.main.FeedPage
import bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeDetailed
import bob.colbaskin.cookly.home.data.models.recipe_detailed.toDomain
import bob.colbaskin.cookly.home.domain.models.main.ActiveCookingSession
import javax.inject.Inject

private const val TAG = "HomeRecipeRepository"

class HomeRecipeRepositoryImpl @Inject constructor(
    private val context: Context,
    private val apiService: HomeRecipeApiService
) : HomeRecipeRepository {

    override suspend fun getRecipeById(recipeId: Int): ApiResult<RecipeDetailed> {
        Log.d(TAG, "Get recipe by id=$recipeId")

        return safeApiCall<RecipeDetailedResponseDto, RecipeDetailed>(
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
        Log.d(TAG, "Get user feed. lastScore=$lastScore, lastId=$lastId, paginationKey=$paginationKey, limit=$limit")

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

    override suspend fun getMealTimeFeed(
        mealTimeType: String,
        lastScore: Double?,
        lastId: Int?,
        paginationKey: String?,
        limit: Int
    ): ApiResult<FeedPage> {
        Log.d(TAG, "Get meal time feed. mealTimeType=$mealTimeType, lastScore=$lastScore, lastId=$lastId, paginationKey=$paginationKey, limit=$limit")

        return safeApiCall<FeedResponseDto, FeedPage>(
            apiCall = {
                apiService.getMealTimeFeed(
                    mealTimeType = mealTimeType,
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

    override suspend fun startCookingSession(recipeId: Int): ApiResult<Int> {
        Log.d(TAG, "Start cooking session for recipeId=$recipeId")

        return  safeApiCall<CookingSessionDto, Int>(
            apiCall = { apiService.startCookingSession(recipeId) },
            successHandler = { response -> response.cooingSessionId },
            context = context
        )
    }

    override suspend fun changeActiveStep(
        cookingSessionId: Int,
        stepNumber: Int
    ): ApiResult<Unit> {
        Log.d(TAG, "Change active step to $stepNumber for cooking session $cookingSessionId")

        return safeApiCall<Unit, Unit>(
            apiCall = { apiService.changeActiveStep(cookingSessionId, stepNumber) },
            successHandler = { response -> response },
            context = context
        )
    }

    override suspend fun cancelCookingSession(cookingSessionId: Int): ApiResult<Unit> {
        Log.d(TAG, "Cancel cooking session with id=$cookingSessionId")

        return safeApiCall<Unit, Unit>(
            apiCall = { apiService.cancelCookingSession(cookingSessionId) },
            successHandler = { response -> response },
            context = context
        )
    }

    override suspend fun finishCookingSession(cookingSessionId: Int): ApiResult<Unit> {
        Log.d(TAG, "Finish cooking session with id=$cookingSessionId")

        return safeApiCall<Unit, Unit>(
            apiCall = { apiService.finishCookingSession(cookingSessionId) },
            successHandler = { response -> response },
            context = context
        )
    }

    override suspend fun getActiveSessions(): ApiResult<List<ActiveCookingSession>> {
        Log.d(TAG, "Get active cooking sessions")

        return safeApiCall<List<ActiveSessionDto>, List<ActiveCookingSession>>(
            apiCall = { apiService.getActiveSessions() },
            successHandler = { response -> response.map { it.toDomain() } },
            context = context
        )
    }
}
