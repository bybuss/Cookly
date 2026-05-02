package bob.colbaskin.cookly.common.recipe_preview.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview

@Composable
fun RecipeListScreen(
    modifier: Modifier = Modifier,
    state: RecipeListState,
    onAction: (RecipeListAction) -> Unit,
    onRecipeClick: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CustomTheme.colors.background)
    ) {
        RecipesDisplayModeSwitcher(
            modifier = Modifier.padding(horizontal = 24.dp),
            selectedMode = state.displayMode,
            onModeClick = { onAction(RecipeListAction.ChangeDisplayMode(it)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        when (val recipesState = state.recipesState) {
            UiState.Idle, UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = CustomTheme.colors.secondAccentColor
                    )
                }
            }
            is UiState.Error -> {
                RecipeListErrorContent(
                    title = recipesState.title,
                    onRetryClick = { onAction(RecipeListAction.LoadRecipes) }
                )
            }
            is UiState.Success<List<RecipePreview>> -> {
                if (recipesState.data.isEmpty()) {
                    EmptyRecipeListContent()
                } else {
                    RecipePreviewList(
                        recipes = recipesState.data,
                        displayMode = state.displayMode,
                        innerPadding = PaddingValues(),
                        onRecipeClick = { recipe ->
                            onAction(RecipeListAction.OpenRecipe(recipe.id))
                            onRecipeClick(recipe.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RecipeListErrorContent(
    title: String,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = CustomTheme.typography.inter.titleMedium,
            color = CustomTheme.colors.text
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRetryClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomTheme.colors.secondAccentColor,
                contentColor = CustomTheme.colors.text
            ),
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "Попробовать снова")
        }
    }
}

@Composable
private fun EmptyRecipeListContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Пока здесь ничего нет",
            style = CustomTheme.typography.inter.bodyMedium,
            color = CustomTheme.colors.secondaryText
        )
    }
}
