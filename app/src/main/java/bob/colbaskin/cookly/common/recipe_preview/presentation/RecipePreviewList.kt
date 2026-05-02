package bob.colbaskin.cookly.common.recipe_preview.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipesDisplayMode
import bob.colbaskin.cookly.home.presentation.components.DishCard
import bob.colbaskin.cookly.home.presentation.components.recommended_dish.RecommendationBanner
import kotlin.math.roundToInt

@Composable
fun RecipePreviewList(
    modifier: Modifier = Modifier,
    recipes: List<RecipePreview>,
    displayMode: RecipesDisplayMode,
    innerPadding: PaddingValues = PaddingValues(),
    onRecipeClick: (RecipePreview) -> Unit
) {
    when (displayMode) {
        RecipesDisplayMode.Banners -> {
            RecipeBannersList(
                modifier = modifier,
                recipes = recipes,
                innerPadding = innerPadding,
                onRecipeClick = onRecipeClick
            )
        }

        RecipesDisplayMode.Cards -> {
            RecipeCardsGrid(
                modifier = modifier,
                recipes = recipes,
                innerPadding = innerPadding,
                onRecipeClick = onRecipeClick
            )
        }
    }
}

@Composable
private fun RecipeCardsGrid(
    modifier: Modifier = Modifier,
    recipes: List<RecipePreview>,
    innerPadding: PaddingValues,
    onRecipeClick: (RecipePreview) -> Unit
) {
    val colors = CustomTheme.colors
    val gridState = rememberLazyGridState()

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
        items(
            items = recipes,
            key = { it.id }
        ) { recipe ->
            DishCard(
                title = recipe.title,
                minutes = recipe.estimatedTime,
                dishImageUrl = recipe.imageUrl,
                rating = recipe.averageRating.roundToOneDecimal(),
                ratingAmount = recipe.ratingCount,
                kcal = recipe.caloriesBy100Grams.toInt(),
                spicyLevel = recipe.spicyLevel,
                difficultyLevel = recipe.difficultyLevel,
                isFlameIconRed = recipe.caloriesBy100Grams >= 300,
                onClick = { onRecipeClick(recipe) }
            )
        }
    }
}

@Composable
private fun RecipeBannersList(
    modifier: Modifier = Modifier,
    recipes: List<RecipePreview>,
    innerPadding: PaddingValues,
    onRecipeClick: (RecipePreview) -> Unit
) {
    val colors = CustomTheme.colors
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = 16.dp)
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        itemsIndexed(
            items = recipes,
            key = { _, recipe -> recipe.id }
        ) { index, recipe ->
            RecommendationBanner(
                cardTitle = recipe.title,
                recipeImageUrl = recipe.imageUrl,
                rating = recipe.averageRating.roundToOneDecimal(),
                ratingAmount = recipe.ratingCount,
                minutes = recipe.estimatedTime,
                kcal = recipe.caloriesBy100Grams.toInt(),
                spicyLevel = recipe.spicyLevel,
                difficultyLevel = recipe.difficultyLevel,
                isFlameIconRed = recipe.caloriesBy100Grams >= 300,
                containerColor = colors.background,
                border = true,
                isLeftCard = index % 2 != 0,
                onOpenClick = { onRecipeClick(recipe) }
            )
        }
    }
}

private fun Double.roundToOneDecimal(): Double {
    return (this * 10.0).roundToInt() / 10.0
}
