package bob.colbaskin.cookly.profile.data

import bob.colbaskin.cookly.common.recipe_preview.data.models.RecipesResponseDto
import bob.colbaskin.cookly.profile.data.models.ProfileUserDto
import retrofit2.http.GET

interface ProfileApiService {

    @GET("/user/me")
    suspend fun getMe(): ProfileUserDto

    @GET("/user/recipe-history")
    suspend fun getRecipeHistory(): RecipesResponseDto
}
