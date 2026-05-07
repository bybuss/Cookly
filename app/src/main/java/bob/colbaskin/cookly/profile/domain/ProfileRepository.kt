package bob.colbaskin.cookly.profile.domain

import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview
import bob.colbaskin.cookly.common.user_prefs.domain.models.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun observeUserPreferences(): Flow<User>
    suspend fun refreshUser(): ApiResult<Unit>
    suspend fun logout(): ApiResult<Unit>
    suspend fun getRecipeHistory(): ApiResult<List<RecipePreview>>

    suspend fun getRecipesOnModeration(): ApiResult<List<RecipePreview>>

    suspend fun getSavedRecipes(): ApiResult<List<RecipePreview>>

    suspend fun getModeratingRecipes(): ApiResult<List<RecipePreview>>

    suspend fun getRejectedRecipes(): ApiResult<List<RecipePreview>>

    suspend fun getPublishedRecipes(): ApiResult<List<RecipePreview>>
}
