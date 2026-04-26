package bob.colbaskin.cookly.create_recipe.domain

import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeCommand

interface CreateRecipeRepository {
    suspend fun submitRecipe(command: CreateRecipeCommand): ApiResult<Int>
}
