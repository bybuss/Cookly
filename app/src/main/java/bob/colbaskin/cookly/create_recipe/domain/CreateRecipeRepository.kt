package bob.colbaskin.cookly.create_recipe.domain

import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeCategory
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeCommand
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeIngredient

interface CreateRecipeRepository {
    suspend fun searchIngredients(query: String): ApiResult<List<CreateRecipeIngredient>>
    suspend fun getRecipeCategories(): ApiResult<List<CreateRecipeCategory>>
    suspend fun submitRecipe(command: CreateRecipeCommand): ApiResult<Int>
}
