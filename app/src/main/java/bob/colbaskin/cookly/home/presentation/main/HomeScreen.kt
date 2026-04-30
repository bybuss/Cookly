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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.common.utils.clickableWithoutRipple
import bob.colbaskin.cookly.home.presentation.components.DishCard
import bob.colbaskin.cookly.home.presentation.components.TopBarWithSearch
import bob.colbaskin.cookly.home.presentation.components.meals.MealsCardRow
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

    LaunchedEffect(state.feedState) {
        if (state.feedState is UiState.Error) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = state.feedState.title,
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
                    navController.navigate(Screens.RecipeDetailed(recipeId = action.recipeId))
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
                    mealsList = state.mealsList
                )
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
                            backgroundImage = R.drawable.shrimp_soup_image,
                            rating = 4.8,
                            ratingAmount = 163,
                            minutes = 20,
                            kcal = 150,
                            isFlameIconRed = false,
                            border = false,
                            backgroundHexColor = "#B9480D",
                            isLeftCard = false
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

            when (state.feedState) {
                UiState.Idle, UiState.Loading -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = colors.accentColor)
                        }
                    }
                }

                is UiState.Error -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = state.feedState.title,
                                style = typography.inter.titleMedium,
                                color = colors.text,
                                textAlign = TextAlign.Center
                            )
                            Button(
                                onClick = { onAction(HomeAction.RefreshFeed) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colors.accentColor,
                                    contentColor = colors.invertedText
                                )
                            ) {
                                Text(text = "Повторить")
                            }
                        }
                    }
                }

                is UiState.Success -> {
                    if (state.recipes.isEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                text = "Рецепты не найдены",
                                style = typography.inter.bodyMedium,
                                color = colors.secondaryText,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        items(
                            count = state.recipes.size,
                            key = { index -> state.recipes[index].id }
                        ) { index ->
                            val recipe = state.recipes[index]

                            LaunchedEffect(
                                key1 = index,
                                key2 = state.recipes.size,
                                key3 = state.isEndReached
                            ) {
                                if (
                                    index >= state.recipes.lastIndex - 4 &&
                                    !state.isEndReached &&
                                    state.appendState !is UiState.Loading
                                ) {
                                    onAction(HomeAction.LoadNextFeedPage)
                                }
                            }

                            DishCard(
                                modifier = Modifier
                                    .clickableWithoutRipple {
                                        onAction(HomeAction.OpenRecipe(recipe.id))
                                    },
                                title = recipe.title,
                                minutes = recipe.estimatedTime,
                                dishImageUrl = recipe.imageUrl,
                                rating = recipe.rating,
                                ratingAmount = recipe.ratingCount,
                                kcal = recipe.caloriesBy100Grams.toInt(),
                                isFlameIconRed = recipe.isFlameIconRed
                            )
                        }

                        if (state.appendState is UiState.Loading) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(28.dp),
                                        color = colors.accentColor,
                                        strokeWidth = 2.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }
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
