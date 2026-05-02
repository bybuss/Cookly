package bob.colbaskin.cookly.favourite.presentation

import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview
import bob.colbaskin.cookly.common.recipe_preview.presentation.BaseRecipeListViewModel
import bob.colbaskin.cookly.favourite.domain.FavouritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavouritesRepository
): BaseRecipeListViewModel() {

    init {
        loadRecipes()
    }

    override suspend fun getRecipes(): ApiResult<List<RecipePreview>> {
        return repository.getFavoriteRecipes()
    }
}
