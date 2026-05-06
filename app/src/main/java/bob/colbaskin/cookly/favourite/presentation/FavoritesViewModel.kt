package bob.colbaskin.cookly.favourite.presentation

import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview
import bob.colbaskin.cookly.common.recipe_preview.presentation.BaseRecipeListViewModel
import bob.colbaskin.cookly.favourite.domain.FavoritesChangeNotifier
import bob.colbaskin.cookly.favourite.domain.FavouritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavouritesRepository,
    private val favoritesChangeNotifier: FavoritesChangeNotifier
) : BaseRecipeListViewModel() {

    init {
        loadRecipes()
        observeFavoritesChanges()
    }

    override suspend fun getRecipes(): ApiResult<List<RecipePreview>> {
        return repository.getFavoriteRecipes()
    }

    private fun observeFavoritesChanges() {
        viewModelScope.launch {
            favoritesChangeNotifier.changes.collectLatest {
                loadRecipes()
            }
        }
    }
}
