package bob.colbaskin.cookly.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.components.feed_pagination.PaginationState
import bob.colbaskin.cookly.common.utils.clickableWithoutRipple
import bob.colbaskin.cookly.home.domain.models.main.FeedRecipe

fun LazyGridScope.paginatedItems(
    state: PaginationState<FeedRecipe>,
    onRetry: () -> Unit,
    onClick: (Int) -> Unit,
    accentColor: Color,
    textColor: Color,
    invertedTextColor: Color,
    secondaryTextColor: Color,
    titleMedium: TextStyle,
    bodyMedium: TextStyle
) {
    when (state.loadState) {
        UiState.Loading -> {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = accentColor)
                }
            }
        }
        is UiState.Error -> {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = state.loadState.title,
                        style = titleMedium,
                        color = textColor,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = onRetry,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor,
                            contentColor = invertedTextColor
                        )
                    ) {
                        Text("Повторить")
                    }
                }
            }
        }
        is UiState.Success -> {
            if (state.items.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        text = "Рецепты не найдены",
                        style = bodyMedium,
                        color = secondaryTextColor,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(
                    count = state.items.size,
                    key = { index -> state.items[index].id }
                ) { index ->
                    val recipe = state.items[index]
                    DishCard(
                        modifier = Modifier.clickableWithoutRipple {
                            onClick(recipe.id)
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
                                color = accentColor,
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }
        }
        else -> Unit
    }
}
