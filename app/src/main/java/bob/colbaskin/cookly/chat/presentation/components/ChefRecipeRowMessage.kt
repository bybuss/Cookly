package bob.colbaskin.cookly.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview
import bob.colbaskin.cookly.home.presentation.components.DishCard

@Composable
fun ChefRecipeRowMessage(
    modifier: Modifier = Modifier,
    recipes: List<RecipePreview>,
    onRecipeClick: (Int) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(end = 16.dp)
    ) {
        items(
            items = recipes,
            key = { it.id }
        ) { recipe ->
            DishCard(
                modifier = modifier,
                title = recipe.title,
                minutes = recipe.estimatedTime,
                dishImageUrl = recipe.imageUrl,
                fallbackImageRes = R.drawable.fallback_avatar,
                rating = recipe.averageRating,
                ratingAmount = recipe.ratingCount,
                kcal = recipe.caloriesBy100Grams.toInt(),
                spicyLevel = recipe.spicyLevel,
                difficultyLevel = recipe.difficultyLevel,
                isFlameIconRed = recipe.isFlameIconRed,
                onClick = { onRecipeClick(recipe.id) }
            )
        }
    }
}
