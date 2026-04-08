package bob.colbaskin.cookly.home.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.home.presentation.components.DishCard
import bob.colbaskin.cookly.home.presentation.components.TopBarWithSearch
import bob.colbaskin.cookly.home.presentation.components.meals.MealsCardRow
import bob.colbaskin.cookly.home.presentation.components.quick_card.QuickCategoryCardRow
import bob.colbaskin.cookly.home.presentation.components.recommended_dish.RecommendationBanner
import bob.colbaskin.cookly.home.presentation.components.recommended_dish.RecommendedDish

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state

    HomeScreen(
        modifier = modifier,
        state = state,
        onAction = { action ->
            when (action) {
                is HomeAction.TestAction -> {}
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
    Scaffold(
        containerColor = CustomTheme.colors.background,
        contentColor = CustomTheme.colors.text,
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
                .background(CustomTheme.colors.background)
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
                    color = CustomTheme.colors.text,
                    style = CustomTheme.typography.madeInfinity.headlineSmall,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Normal
                )
            }
            items(20) {
                DishCard(
                    modifier = Modifier,
                    title = "Fried Shrimp",
                    minutes = 20,
                    dishImage = R.drawable.fried_egg_backgroiund,
                    rating = 4.8,
                    ratingAmount = 168,
                    kcal = 150,
                    isFlameIconRed = false,
                )
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
