package bob.colbaskin.cookly.create_recipe.data

import bob.colbaskin.cookly.create_recipe.data.models.CreateRecipeRequestDto
import bob.colbaskin.cookly.create_recipe.data.models.IngredientSearchResponseDto
import bob.colbaskin.cookly.create_recipe.data.models.RecipeCategoryDto
import bob.colbaskin.cookly.create_recipe.data.models.RecipeResponseDto
import bob.colbaskin.cookly.create_recipe.data.models.UploadImageResponseDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface CreateRecipeApiService {

    @GET("/ingredient/search")
    suspend fun searchIngredients(
        @Query("query") query: String
    ): List<IngredientSearchResponseDto>

    @GET("/recipe-category/list")
    suspend fun getRecipeCategories(): List<RecipeCategoryDto>

    @POST("/recipe")
    suspend fun createRecipe(
        @Body request: CreateRecipeRequestDto
    ): RecipeResponseDto

    @Multipart
    @POST("/recipe/{recipe_id}/avatar")
    suspend fun uploadRecipeAvatar(
        @Path("recipe_id") recipeId: Int,
        @Part file: MultipartBody.Part
    ): UploadImageResponseDto

    @Multipart
    @POST("/recipe/{recipe_id}/{recipe_step_number}/avatar")
    suspend fun uploadRecipeStepAvatar(
        @Path("recipe_id") recipeId: Int,
        @Path("recipe_step_number") recipeStepNumber: Int,
        @Part file: MultipartBody.Part
    ): UploadImageResponseDto
}
