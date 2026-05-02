package bob.colbaskin.cookly.home.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.components.feed_pagination.PaginationEffect
import bob.colbaskin.cookly.common.components.feed_pagination.PaginationState
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.common.utils.clickableWithoutRipple
import bob.colbaskin.cookly.home.domain.models.main.ActiveCookingSession
import bob.colbaskin.cookly.home.presentation.components.ActiveSessionBanner
import bob.colbaskin.cookly.home.presentation.components.DishCard
import bob.colbaskin.cookly.home.presentation.components.TopBarWithSearch
import bob.colbaskin.cookly.home.presentation.components.meals.MealsCardRow
import bob.colbaskin.cookly.home.presentation.components.paginatedItems
import bob.colbaskin.cookly.home.presentation.components.quick_card.QuickCategoryCardRow
import bob.colbaskin.cookly.home.presentation.components.recommended_dish.RecommendationBanner
import bob.colbaskin.cookly.home.presentation.components.recommended_dish.RecommendedDish
import bob.colbaskin.cookly.navigation.Screens
import kotlinx.coroutines.launch

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.feedPagination.loadState) {
        if (state.feedPagination.loadState is UiState.Error) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = state.feedPagination.loadState.title,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    LaunchedEffect(state.activeCookingSessions) {
        if (state.activeCookingSessions is UiState.Error) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = state.activeCookingSessions.title,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    HomeScreen(
        modifier = modifier,
        state = state,
        onAction = { action ->
            when (action) {
                is HomeAction.OpenRecipe -> {
                    navController.navigate(Screens.RecipeDetailed(action.recipeId))
                }

                is HomeAction.OpenMealTimeDetailed -> {
                    navController.navigate(Screens.MealTimeDetailed(action.mealTimeType))
                }

                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography
    val gridState = rememberLazyGridState()

    PaginationEffect(
        gridState = gridState,
        itemCount = state.feedPagination.items.size,
        appendState = state.feedPagination.appendState,
        isEndReached = state.feedPagination.isEndReached,
        enabled = state.feedPagination.loadState is UiState.Success,
        onLoadNext = { onAction(HomeAction.LoadNextFeedPage) }
    )

    Scaffold(
        containerColor = colors.background,
        contentColor = colors.text,
        topBar = {
            TopBarWithSearch(
                modifier = Modifier.padding(16.dp),
                value = "",
                onValueChange = {}
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            state = gridState,
            modifier = modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(horizontal = 16.dp)
                .padding(innerPadding),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                QuickCategoryCardRow(
                    modifier = Modifier,
                    quickCardsList = state.quickCardsList
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                MealsCardRow(
                    modifier = Modifier,
                    mealsList = state.mealsList,
                    onClick = { mealTimeType ->
                        onAction(HomeAction.OpenMealTimeDetailed(mealTimeType))
                    }
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                when (val sessionsState = state.activeCookingSessions) {
                    UiState.Idle, UiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = colors.accentColor)
                        }
                    }

                    is UiState.Success -> {
                        sessionsState.data?.let {
                            ActiveSessionsRow(
                                sessions = it,
                                onCancelClick = { sessionId ->
                                    onAction(HomeAction.CancelActiveSession(sessionId))
                                },
                                onOpenClick = { recipeId ->
                                    onAction(HomeAction.OpenRecipe(recipeId))
                                }
                            )
                        }
                    }

                    else -> Unit
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                RecommendedDish(
                    modifier = Modifier,
                    title = "Блюдо от Шефа",
                    aiAvatar = R.drawable.cheif_ai_avatar,
                    recommendationCard = {
                        RecommendationBanner(
                            modifier = Modifier,
                            cardTitle = "Fried Shrimp",
                            recipeImageUrl = "",
                            rating = 4.8,
                            ratingAmount = 163,
                            minutes = 20,
                            kcal = 150,
                            isFlameIconRed = false,
                            border = false,
                            backgroundHexColor = "#B9480D",
                            isLeftCard = false,
                            onOpenClick = {}
                        )
                    }
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Рецепты",
                    color = colors.text,
                    style = typography.madeInfinity.headlineSmall,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Normal
                )
            }

            paginatedItems(
                state = state.feedPagination,
                onRetry = { onAction(HomeAction.RefreshFeed) },
                onClick = { recipeId ->
                    onAction(HomeAction.OpenRecipe(recipeId))
                },
                accentColor = colors.accentColor,
                textColor = colors.text,
                invertedTextColor = colors.invertedText,
                secondaryTextColor = colors.secondaryText,
                titleMedium = typography.inter.titleMedium,
                bodyMedium = typography.inter.bodyMedium
            )
        }
    }
}

@Composable
fun ActiveSessionsRow(
    sessions: List<ActiveCookingSession>,
    onCancelClick: (Int) -> Unit,
    onOpenClick: (Int) -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(sessions, key = { it.sessionId }) { session ->
            ActiveSessionBanner(
                session = session,
                onCancelClick = onCancelClick,
                onOpenClick = onOpenClick,
                modifier = Modifier.width(screenWidth - 32.dp)
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    UfoodTheme {
        HomeScreen(
            modifier = Modifier,
            state = HomeState(),
            onAction = {}
        )
    }
}
