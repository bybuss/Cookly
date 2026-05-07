package bob.colbaskin.cookly.profile.data

import bob.colbaskin.cookly.common.recipe_preview.data.models.RecipesResponseDto
import bob.colbaskin.cookly.profile.data.models.ProfileUserDto
import retrofit2.http.GET

interface ProfileApiService {

    @GET("/user/me")
    suspend fun getMe(): ProfileUserDto

    @GET("/user/recipe-history")
    suspend fun getRecipeHistory(): RecipesResponseDto

    @GET("/recipes/on-moderation")
    suspend fun getRecipesOnModeration(): RecipesResponseDto

    @GET("/user/saved-recipes")
    suspend fun getSavedRecipes(): RecipesResponseDto

    @GET("/user/moderating-recipes")
    suspend fun getModeratingRecipes(): RecipesResponseDto

    @GET("/user/rejected-recipes")
    suspend fun getRejectedRecipes(): RecipesResponseDto

    @GET("/user/published-recipes")
    suspend fun getPublishedRecipes(): RecipesResponseDto
}
