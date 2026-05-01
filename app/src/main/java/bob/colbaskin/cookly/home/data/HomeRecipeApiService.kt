package bob.colbaskin.cookly.home.data

import bob.colbaskin.cookly.home.data.models.main.FeedResponseDto
import bob.colbaskin.cookly.home.data.models.recipe_detailed.CookingSessionDto
import bob.colbaskin.cookly.home.data.models.recipe_detailed.RecipeDetailedDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeRecipeApiService {

    @GET("/recipe/{recipe_id}")
    suspend fun getRecipeById(
        @Path("recipe_id") recipeId: Int
    ): RecipeDetailedDto

    @GET("/user/feed")
    suspend fun getUserFeed(
        @Query("last_score") lastScore: Double? = null,
        @Query("last_id") lastId: Int? = null,
        @Query("pagination_key") paginationKey: String? = null,
        @Query("limit") limit: Int = 20
    ): FeedResponseDto

    @POST("/recipe/{recipe_id}/cooking_session/start")
    suspend fun startCookingSession(
        @Path("recipe_id") recipeId: Int
    ): CookingSessionDto
}
