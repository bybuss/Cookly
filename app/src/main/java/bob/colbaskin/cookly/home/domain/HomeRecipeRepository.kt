package bob.colbaskin.cookly.home.domain

import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeDetailed

interface HomeRecipeRepository {
    suspend fun getRecipeById(recipeId: Int): ApiResult<RecipeDetailed>
}
