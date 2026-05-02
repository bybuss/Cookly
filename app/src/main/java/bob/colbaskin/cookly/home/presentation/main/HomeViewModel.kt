package bob.colbaskin.cookly.home.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.toUiState
import bob.colbaskin.cookly.home.domain.HomeRecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val FEED_LIMIT = 20

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRecipeRepository
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    init {
        loadInitialFeed()
        loadActiveSessions()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.LoadInitialFeed -> loadInitialFeed()
            HomeAction.RefreshFeed -> refreshFeed()
            HomeAction.LoadNextFeedPage -> loadNextFeedPage()
            is HomeAction.CancelActiveSession -> cancelSession(action.sessionId)
            else -> Unit
        }
    }

    private fun loadInitialFeed() {
        if (state.feedState is UiState.Loading) return

        state = state.copy(feedState = UiState.Loading)

        viewModelScope.launch {
            state = state.copy(
                feedState = UiState.Loading,
                appendState = UiState.Idle,
                recipes = emptyList(),
                lastRecipeScore = null,
                lastRecipeId = null,
                paginationKey = null,
                isEndReached = false
            )

            val result = repository.getUserFeed(
                lastScore = null,
                lastId = null,
                paginationKey = null,
                limit = FEED_LIMIT
            )

            when (result) {
                is ApiResult.Success -> {
                    val page = result.data

                    state = state.copy(
                        feedState = UiState.Success(page.recipes),
                        recipes = page.recipes,
                        lastRecipeScore = page.lastRecipeScore,
                        lastRecipeId = page.lastRecipeId,
                        paginationKey = page.paginationKey,
                        isEndReached = page.paginationKey == null
                    )
                }

                is ApiResult.Error -> {
                    state = state.copy(feedState = result.toUiState())
                }
            }
        }
    }

    private fun loadActiveSessions() {
        if (state.activeCookingSessions is UiState.Loading) return

        state = state.copy(activeCookingSessions = UiState.Loading)

        viewModelScope.launch {
            val result = repository.getActiveSessions()
            state = state.copy(activeCookingSessions = result.toUiState())
        }
    }

    private fun refreshFeed() {
        viewModelScope.launch {
            state = state.copy(
                feedState = UiState.Loading,
                appendState = UiState.Idle,
                lastRecipeScore = null,
                lastRecipeId = null,
                paginationKey = null,
                isEndReached = false
            )

            val result = repository.getUserFeed(
                lastScore = null,
                lastId = null,
                paginationKey = null,
                limit = FEED_LIMIT
            )

            when (result) {
                is ApiResult.Success -> {
                    val page = result.data

                    state = state.copy(
                        feedState = UiState.Success(page.recipes),
                        recipes = page.recipes,
                        lastRecipeScore = page.lastRecipeScore,
                        lastRecipeId = page.lastRecipeId,
                        paginationKey = page.paginationKey,
                        isEndReached = page.paginationKey == null
                    )
                }

                is ApiResult.Error -> {
                    state = state.copy(feedState = result.toUiState())
                }
            }
        }
    }

    private fun loadNextFeedPage() {
        if (state.isEndReached) return
        if (state.appendState is UiState.Loading) return
        if (state.feedState is UiState.Loading) return
        if (state.paginationKey == null) return

        viewModelScope.launch {
            state = state.copy(appendState = UiState.Loading)

            val result = repository.getUserFeed(
                lastScore = state.lastRecipeScore,
                lastId = state.lastRecipeId,
                paginationKey = state.paginationKey,
                limit = FEED_LIMIT
            )

            when (result) {
                is ApiResult.Success -> {
                    val page = result.data
                    val mergedRecipes = (state.recipes + page.recipes)
                        .distinctBy { it.id }

                    state = state.copy(
                        recipes = mergedRecipes,
                        feedState = UiState.Success(mergedRecipes),
                        appendState = UiState.Success(Unit),
                        lastRecipeScore = page.lastRecipeScore,
                        lastRecipeId = page.lastRecipeId,
                        paginationKey = page.paginationKey,
                        isEndReached = page.paginationKey == null
                    )
                }

                is ApiResult.Error -> {
                    state = state.copy(appendState = result.toUiState())
                }
            }
        }
    }

    private fun cancelSession(sessionId: Int) {
        val currentSessions = (state.activeCookingSessions as? UiState.Success)?.data.orEmpty()
        state = state.copy(
            activeCookingSessions = UiState.Success(
                currentSessions.filter { it.sessionId != sessionId }
            )
        )
        viewModelScope.launch {
            repository.cancelCookingSession(sessionId)
        }
    }
}
