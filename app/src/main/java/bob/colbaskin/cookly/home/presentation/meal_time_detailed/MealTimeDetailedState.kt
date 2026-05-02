package bob.colbaskin.cookly.home.presentation.meal_time_detailed

import bob.colbaskin.cookly.common.components.feed_pagination.PaginationState
import bob.colbaskin.cookly.common.utils.getFirstLetter
import bob.colbaskin.cookly.home.domain.models.main.FeedRecipe
import bob.colbaskin.cookly.home.domain.models.meal.MealFeedItem

data class MealDetailedState(
    val email: String = "",
    val avatarUrl: String = "",

    val carouselItems: List<MealFeedItem> = emptyList(),
    val pagination: PaginationState<FeedRecipe> = PaginationState(),
    val mealTimeType: String = "",
    val currentPage: Int = 0,
    val isSheetExpanded: Boolean = false,
    val isAutoScrollEnabled: Boolean = true
) {
    val avatarLetter: String
        get() = email.getFirstLetter()
}
