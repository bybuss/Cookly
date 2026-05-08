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
import bob.colbaskin.cookly.common.toUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseRecipeListViewModel: ViewModel() {

    var state by mutableStateOf(RecipeListState())
        private set
    private var loadJob: Job? = null

    fun onAction(action: RecipeListAction) {
        when (action) {
            RecipeListAction.LoadRecipes -> loadRecipes()
            RecipeListAction.Refresh -> refreshRecipes()
            is RecipeListAction.ChangeDisplayMode -> changeDisplayMode(mode = action.mode)
            is RecipeListAction.OpenRecipe -> onOpenRecipe(action.id)
        }
    }

    protected fun loadRecipes() {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            state = state.copy(recipesState = UiState.Loading)
            val result = getRecipes().toUiState()
            state = state.copy(
                recipesState = result,
                isRefreshing = false
            )
        }
    }

    private fun refreshRecipes() {
        if (state.isRefreshing) return

        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            state = state.copy(isRefreshing = true)
            val result = getRecipes().toUiState()
            state = state.copy(
                recipesState = result,
                isRefreshing = false
            )
        }
    }

    protected fun changeDisplayMode(mode: RecipesDisplayMode) {
        state = state.copy(displayMode = mode)
    }

    protected abstract suspend fun getRecipes(): ApiResult<List<RecipePreview>>

    protected open fun onOpenRecipe(id: Int) = Unit
}
