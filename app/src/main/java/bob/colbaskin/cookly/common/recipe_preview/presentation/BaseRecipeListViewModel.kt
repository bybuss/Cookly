package bob.colbaskin.cookly.common.recipe_preview.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipesDisplayMode
import kotlinx.coroutines.launch

abstract class BaseRecipeListViewModel: ViewModel() {

    var state by mutableStateOf(RecipeListState())
        private set

    fun onAction(action: RecipeListAction) {
        when (action) {
            RecipeListAction.LoadRecipes -> loadRecipes()
            is RecipeListAction.ChangeDisplayMode -> changeDisplayMode(mode = action.mode)
            is RecipeListAction.OpenRecipe -> onOpenRecipe(action.id)
        }
    }

    protected fun loadRecipes() {
        viewModelScope.launch {
            state = state.copy(recipesState = UiState.Loading)

            state = when (val result = getRecipes()) {
                is ApiResult.Success -> {
                    state.copy(
                        recipesState = UiState.Success(result.data)
                    )
                }
                is ApiResult.Error -> {
                    state.copy(
                        recipesState = UiState.Error(
                            title = result.title,
                            text = result.text
                        )
                    )
                }
            }
        }
    }

    protected fun changeDisplayMode(mode: RecipesDisplayMode) {
        state = state.copy(displayMode = mode)
    }

    protected abstract suspend fun getRecipes(): ApiResult<List<RecipePreview>>

    protected open fun onOpenRecipe(id: Int) = Unit
}
