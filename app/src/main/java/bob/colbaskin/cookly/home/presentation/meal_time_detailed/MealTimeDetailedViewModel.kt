package bob.colbaskin.cookly.home.presentation.meal_time_detailed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.cookly.common.components.feed_pagination.FeedPaginator
import bob.colbaskin.cookly.home.data.models.main.toMealFeedItem
import bob.colbaskin.cookly.home.domain.HomeRecipeRepository
import bob.colbaskin.cookly.profile.domain.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val CAROUSEL_COUNT = 4

@HiltViewModel
class MealTimeDetailedViewModel @Inject constructor(
    private val homeRepository: HomeRecipeRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    var state by mutableStateOf(MealDetailedState())
        private set

    private lateinit var paginator: FeedPaginator

    init {
        observeLocalUser()
    }

    fun onAction(action: MealTimeDetailedAction) {
        when (action) {
            is MealTimeDetailedAction.OnPagerPageSettled -> {
                state = state.copy(currentPage = action.page)
            }
            is MealTimeDetailedAction.OnSheetStateChanged -> {
                state = state.copy(
                    isSheetExpanded = action.isExpanded,
                    isAutoScrollEnabled = !action.isExpanded
                )
            }
            MealTimeDetailedAction.LoadNextPage -> {
                loadNextPage()
            }
            MealTimeDetailedAction.Refresh -> {
                loadMealTimeFeed(state.mealTimeType)
            }
            else -> Unit
        }
    }

    fun loadMealTimeFeed(mealTimeType: String) {
        state = state.copy(mealTimeType = mealTimeType)

        paginator = FeedPaginator { lastScore, lastId, key ->
            homeRepository.getMealTimeFeed(
                mealTimeType,
                lastScore,
                lastId,
                key,
                20
            )
        }

        viewModelScope.launch {
            val firstPage = paginator.loadFirst()
            val carousel = firstPage.items.take(CAROUSEL_COUNT).map { it.toMealFeedItem() }
            val rest = firstPage.items.drop(CAROUSEL_COUNT)

            state = state.copy(
                carouselItems = carousel,
                pagination = firstPage.copy(items = rest)
            )
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            val updated = paginator.loadNext(state.pagination)
            state = state.copy(pagination = updated)
        }
    }

    private fun observeLocalUser() {
        viewModelScope.launch {
            profileRepository.observeUserPreferences().collectLatest { prefs ->
                state = state.copy(
                    email = prefs.email,
                    avatarUrl = prefs.avatarUrl
                )
            }
        }
    }
}
