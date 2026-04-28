package bob.colbaskin.cookly.home.data

import bob.colbaskin.cookly.home.data.models.recipe_detailed.RecipeDetailedDto
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeRecipeApiService {

    @GET("/recipe/{recipe_id}")
    suspend fun getRecipeById(
        @Path("recipe_id") recipeId: Int
    ): RecipeDetailedDto
}
