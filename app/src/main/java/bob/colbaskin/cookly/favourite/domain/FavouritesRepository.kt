package bob.colbaskin.cookly.favourite.domain

import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview

interface FavouritesRepository {
    suspend fun getFavoriteRecipes(): ApiResult<List<RecipePreview>>
}
