package bob.colbaskin.cookly.search_result.domain

import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview
import bob.colbaskin.cookly.search_result.domain.models.RecipeSearchFilters

interface SearchRecipeRepository {

    suspend fun searchRecipes(
        filters: RecipeSearchFilters,
        limit: Int = 100,
        offset: Int = 0
    ): ApiResult<List<RecipePreview>>
}
