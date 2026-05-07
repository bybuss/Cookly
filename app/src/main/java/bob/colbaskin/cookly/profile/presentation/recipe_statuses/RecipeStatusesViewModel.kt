package bob.colbaskin.cookly.profile.presentation.recipe_statuses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipesDisplayMode
import bob.colbaskin.cookly.common.recipe_preview.presentation.RecipeListState
import bob.colbaskin.cookly.profile.domain.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeStatusesViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    var state by mutableStateOf(RecipeStatusesState())
        private set

    init {
        loadTab(RecipeStatusesTab.Saved)
    }

    fun onAction(action: RecipeStatusesAction) {
        when (action) {
            is RecipeStatusesAction.SelectTab -> {
                state = state.copy(selectedTab = action.tab)
                loadTab(tab = action.tab)
            }

            is RecipeStatusesAction.LoadTab -> {
                loadTab(
                    tab = action.tab,
                    forceRefresh = action.forceRefresh
                )
            }

            is RecipeStatusesAction.RefreshTab -> {
                loadTab(
                    tab = action.tab,
                    forceRefresh = true
                )
            }

            is RecipeStatusesAction.ChangeDisplayMode -> {
                changeDisplayMode(
                    tab = action.tab,
                    mode = action.mode
                )
            }

            is RecipeStatusesAction.OpenRecipe -> Unit
        }
    }

    private fun loadTab(
        tab: RecipeStatusesTab,
        forceRefresh: Boolean = false
    ) {
        val currentTabState = state.stateFor(tab)

        if (!forceRefresh && tab in state.loadedTabs) return
        if (currentTabState.recipesState is UiState.Loading) return

        updateTabState(tab) {
            it.copy(recipesState = UiState.Loading)
        }

        viewModelScope.launch {
            val result = getRecipesByTab(tab)

            updateTabState(tab) { oldState ->
                when (result) {
                    is ApiResult.Success -> {
                        oldState.copy(
                            recipesState = UiState.Success(result.data)
                        )
                    }

                    is ApiResult.Error -> {
                        oldState.copy(
                            recipesState = UiState.Error(
                                title = result.title,
                                text = result.text
                            )
                        )
                    }
                }
            }

            if (result is ApiResult.Success) {
                state = state.copy(
                    loadedTabs = state.loadedTabs + tab
                )
            }
        }
    }

    private suspend fun getRecipesByTab(
        tab: RecipeStatusesTab
    ): ApiResult<List<RecipePreview>> {
        return when (tab) {
            RecipeStatusesTab.Saved -> {
                repository.getSavedRecipes()
            }

            RecipeStatusesTab.Moderating -> {
                repository.getModeratingRecipes()
            }

            RecipeStatusesTab.Rejected -> {
                repository.getRejectedRecipes()
            }

            RecipeStatusesTab.Published -> {
                repository.getPublishedRecipes()
            }
        }
    }

    private fun changeDisplayMode(
        tab: RecipeStatusesTab,
        mode: RecipesDisplayMode
    ) {
        updateTabState(tab) {
            it.copy(displayMode = mode)
        }
    }

    private fun updateTabState(
        tab: RecipeStatusesTab,
        transform: (RecipeListState) -> RecipeListState
    ) {
        val currentMap = state.tabStates
        val currentTabState = currentMap[tab] ?: RecipeListState()

        state = state.copy(
            tabStates = currentMap + (tab to transform(currentTabState))
        )
    }
}
