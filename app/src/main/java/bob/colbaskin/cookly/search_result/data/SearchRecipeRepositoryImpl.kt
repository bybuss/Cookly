package bob.colbaskin.cookly.search_result.data

import android.content.Context
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.recipe_preview.data.models.RecipesResponseDto
import bob.colbaskin.cookly.common.recipe_preview.data.models.toDomain
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview
import bob.colbaskin.cookly.common.utils.safeApiCall
import bob.colbaskin.cookly.search_result.domain.SearchRecipeRepository
import bob.colbaskin.cookly.search_result.domain.models.RecipeSearchFilters
import javax.inject.Inject

class SearchRecipeRepositoryImpl @Inject constructor(
    private val context: Context,
    private val apiService: SearchRecipeApiService
) : SearchRecipeRepository {

    override suspend fun searchRecipes(
        filters: RecipeSearchFilters,
        limit: Int,
        offset: Int
    ): ApiResult<List<RecipePreview>> {
        return safeApiCall<RecipesResponseDto, List<RecipePreview>>(
            apiCall = {
                apiService.searchRecipes(
                    query = filters.query.takeIf { it.isNotBlank() },
                    includeIngredientGroupIds = filters.includeIngredientGroupIds.takeIf { it.isNotEmpty() },
                    maxSpicy = filters.maxSpicy,
                    maxDifficulty = filters.maxDifficulty,
                    maxCaloriesBy100Grams = filters.maxCaloriesBy100Grams,
                    mealTimeType = filters.mealTimeType?.apiValue,
                    minAvgRating = filters.minAvgRating,
                    maxEstimatedCookingTime = filters.maxEstimatedCookingTime,
                    limit = limit,
                    offset = offset
                )
            },
            successHandler = { response ->
                response.recipes.map { it.toDomain() }
            },
            context = context
        )
    }
}
