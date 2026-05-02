package bob.colbaskin.cookly.favourite.data

import bob.colbaskin.cookly.common.recipe_preview.data.models.RecipesResponseDto
import retrofit2.http.GET

interface FavouritesApiService {
    @GET("/user/favorite-recipes")
    suspend fun getFavoriteRecipes(): RecipesResponseDto
}
