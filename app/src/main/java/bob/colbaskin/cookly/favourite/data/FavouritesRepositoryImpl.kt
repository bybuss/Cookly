package bob.colbaskin.cookly.favourite.data

import android.content.Context
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.recipe_preview.data.models.RecipesResponseDto
import bob.colbaskin.cookly.common.recipe_preview.data.models.toDomain
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview
import bob.colbaskin.cookly.common.utils.safeApiCall
import bob.colbaskin.cookly.favourite.domain.FavouritesRepository
import javax.inject.Inject

class FavouritesRepositoryImpl @Inject constructor(
    private val context: Context,
    private val apiService: FavouritesApiService
): FavouritesRepository {
    override suspend fun getFavoriteRecipes(): ApiResult<List<RecipePreview>> {
        return safeApiCall<RecipesResponseDto, List<RecipePreview>>(
            apiCall = { apiService.getFavoriteRecipes() },
            successHandler = { response -> response.recipes.map { it.toDomain() } },
            context = context
        )
    }
}
