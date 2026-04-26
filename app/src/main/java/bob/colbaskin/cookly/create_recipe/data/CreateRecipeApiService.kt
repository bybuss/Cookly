package bob.colbaskin.cookly.create_recipe.data

import bob.colbaskin.cookly.create_recipe.data.models.CreateRecipeRequestDto
import bob.colbaskin.cookly.create_recipe.data.models.RecipeResponseDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface CreateRecipeApiService {

    @POST("/recipe")
    suspend fun createRecipe(
        @Body request: CreateRecipeRequestDto
    ): RecipeResponseDto

    @Multipart
    @POST("/recipe/{recipe_id}/avatar")
    suspend fun uploadRecipeAvatar(
        @Path("recipe_id") recipeId: Int,
        @Part file: MultipartBody.Part
    ): String

    @Multipart
    @POST("/recipe/{recipe_id}/{recipe_step_id}/avatar")
    suspend fun uploadRecipeStepAvatar(
        @Path("recipe_id") recipeId: Int,
        @Path("recipe_step_id") recipeStepId: Int,
        @Part file: MultipartBody.Part
    ): String
}
