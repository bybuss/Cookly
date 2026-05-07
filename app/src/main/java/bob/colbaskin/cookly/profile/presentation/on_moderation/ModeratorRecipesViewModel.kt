package bob.colbaskin.cookly.profile.presentation.on_moderation

import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview
import bob.colbaskin.cookly.common.recipe_preview.presentation.BaseRecipeListViewModel
import bob.colbaskin.cookly.profile.domain.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ModeratorRecipesViewModel @Inject constructor(
    private val repository: ProfileRepository
): BaseRecipeListViewModel() {

    init {
        loadRecipes()
    }

    override suspend fun getRecipes(): ApiResult<List<RecipePreview>> {
        return repository.getRecipesOnModeration()
    }
}
