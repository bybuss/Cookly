package bob.colbaskin.cookly.home.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.components.feed_pagination.FeedPaginator
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

    private lateinit var paginator: FeedPaginator

    init {
        loadInitialFeed()
        loadActiveSessions()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.LoadInitialFeed -> loadInitialFeed()
            HomeAction.RefreshFeed -> loadInitialFeed()
            HomeAction.LoadNextFeedPage -> loadNextFeedPage()
            is HomeAction.CancelActiveSession -> cancelSession(action.sessionId)
            else -> Unit
        }
    }

    private fun buildPaginator() {
        paginator = FeedPaginator { lastScore, lastId, key ->
            repository.getUserFeed(
                lastScore = lastScore,
                lastId = lastId,
                paginationKey = key,
                limit = FEED_LIMIT
            )
        }
    }

    private fun loadInitialFeed() {
        if (state.feedPagination.loadState is UiState.Loading) return

        buildPaginator()

        state = state.copy(
            feedPagination = state.feedPagination.copy(
                loadState = UiState.Loading,
                appendState = UiState.Idle,
                items = emptyList(),
                lastScore = null,
                lastId = null,
                paginationKey = null,
                isEndReached = false
            )
        )

        viewModelScope.launch {
            val result = paginator.loadFirst()
            state = state.copy(feedPagination = result)
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

    private fun loadNextFeedPage() {
        if (!::paginator.isInitialized) return

        val current = state.feedPagination
        if (current.isEndReached) return
        if (current.appendState is UiState.Loading) return
        if (current.loadState is UiState.Loading) return
        if (current.paginationKey == null) return

        viewModelScope.launch {
            val updated = paginator.loadNext(current)
            state = state.copy(feedPagination = updated)
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
