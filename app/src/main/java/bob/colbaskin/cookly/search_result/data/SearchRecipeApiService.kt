package bob.colbaskin.cookly.search_result.data

import bob.colbaskin.cookly.common.recipe_preview.data.models.RecipesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchRecipeApiService {

    @GET("/recipe/search")
    suspend fun searchRecipes(
        @Query("query") query: String? = null,
        @Query("include_ingredient_group_ids") includeIngredientGroupIds: List<Int>? = null,
        @Query("max_spicy") maxSpicy: Int? = null,
        @Query("max_difficulty") maxDifficulty: Int? = null,
        @Query("max_calories_by_100grams") maxCaloriesBy100Grams: Double? = null,
        @Query("meal_time_type") mealTimeType: String? = null,
        @Query("min_avg_rating") minAvgRating: Double? = null,
        @Query("max_estimated_cooking_time") maxEstimatedCookingTime: Int? = null,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): RecipesResponseDto
}
