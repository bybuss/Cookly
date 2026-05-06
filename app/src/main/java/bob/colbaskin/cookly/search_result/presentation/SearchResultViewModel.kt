package bob.colbaskin.cookly.search_result.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.recipe_preview.data.models.toMealTimeType
import bob.colbaskin.cookly.common.toUiState
import bob.colbaskin.cookly.onboarding_preferences.domain.OnboardingPreferencesRepository
import bob.colbaskin.cookly.search_result.domain.SearchRecipeRepository
import bob.colbaskin.cookly.search_result.domain.models.RecipeSearchFilters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val searchRepository: SearchRecipeRepository,
    private val onboardingPreferencesRepository: OnboardingPreferencesRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var state by mutableStateOf(SearchResultState())
        private set

    private var searchJob: Job? = null

    init {
        val query = savedStateHandle.get<String>("query").orEmpty()
        val mealTimeType = savedStateHandle.get<String>("mealTimeType")
            ?.takeIf { it.isNotBlank() }
            ?.toMealTimeType()

        val initialFilters = RecipeSearchFilters(query = query, mealTimeType = mealTimeType)

        state = state.copy(
            searchText = query,
            appliedFilters = initialFilters,
            draftFilters = initialFilters
        )

        loadIngredientGroups()
        search()
    }

    fun onAction(action: SearchResultAction) {
        when (action) {
            SearchResultAction.LoadInitialData -> {
                loadIngredientGroups()
                search()
            }
            SearchResultAction.Search -> search()
            is SearchResultAction.ChangeSearchText -> {
                state = state.copy(searchText = action.value)
            }
            SearchResultAction.OpenFiltersSheet -> {
                state = state.copy(
                    isFiltersSheetVisible = true,
                    draftFilters = state.appliedFilters.copy(
                        query = state.searchText
                    )
                )
            }
            SearchResultAction.CloseFiltersSheet -> state = state.copy(isFiltersSheetVisible = false)
            is SearchResultAction.ChangeDraftFilters -> {
                state = state.copy(draftFilters = action.filters)
            }
            SearchResultAction.ResetDraftFilters -> {
                state = state.copy(
                    draftFilters = RecipeSearchFilters(
                        query = state.searchText
                    )
                )
            }
            SearchResultAction.ApplyDraftFilters -> applyDraftFilters()
            is SearchResultAction.ChangeDisplayMode -> state = state.copy(displayMode = action.mode)
            else -> Unit
        }
    }

    private fun loadIngredientGroups() {
        if (state.ingredientGroupsState is UiState.Loading) return
        if (state.ingredientGroupsState is UiState.Success) return

        state = state.copy(ingredientGroupsState = UiState.Loading)

        viewModelScope.launch {
            val result = onboardingPreferencesRepository.getIngredientGroups().toUiState()
            state = state.copy(ingredientGroupsState = result)
        }
    }

    private fun search() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val filters = state.appliedFilters.copy(
                query = state.searchText
            )
            state = state.copy(
                recipesState = UiState.Loading,
                appliedFilters = filters,
                draftFilters = state.draftFilters.copy(query = state.searchText)
            )
            val result = searchRepository.searchRecipes(filters).toUiState()
            state = state.copy(recipesState = result)
        }
    }

    private fun applyDraftFilters() {
        val newFilters = state.draftFilters.copy(query = state.searchText)
        state = state.copy(
            appliedFilters = newFilters,
            draftFilters = newFilters,
            isFiltersSheetVisible = false
        )
        search()
    }
}
