package bob.colbaskin.cookly.search_result.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview
import bob.colbaskin.cookly.common.recipe_preview.presentation.RecipePreviewList
import bob.colbaskin.cookly.common.recipe_preview.presentation.RecipesDisplayModeSwitcher
import bob.colbaskin.cookly.home.presentation.components.TopBarWithSearch
import bob.colbaskin.cookly.navigation.Screens
import bob.colbaskin.cookly.search_result.presentation.components.SearchFiltersBottomSheet

@Composable
fun SearchResultScreenRoot(
    navController: NavHostController,
    viewModel: SearchResultViewModel = hiltViewModel()
) {
    SearchResultScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is SearchResultAction.OpenRecipe -> {
                    navController.navigate(Screens.RecipeDetailed(action.recipeId))
                }
                else -> Unit
            }
            viewModel.onAction(action)
        },
        onBackClick = { navController.popBackStack() }
    )
}

@Composable
private fun SearchResultScreen(
    state: SearchResultState,
    onAction: (SearchResultAction) -> Unit,
    onBackClick: () -> Unit
) {
    val colors = CustomTheme.colors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
        ) {
            Row(
                modifier = Modifier.padding(
                    start = 8.dp,
                    end = 16.dp,
                    top = 16.dp
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.size(40.dp),
                    onClick = onBackClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_left),
                        contentDescription = null,
                        tint = colors.text
                    )
                }

                TopBarWithSearch(
                    modifier = Modifier.weight(1f),
                    value = state.searchText,
                    onValueChange = {
                        onAction(SearchResultAction.ChangeSearchText(it))
                    },
                    onSearchClick = {
                        onAction(SearchResultAction.Search)
                    },
                    onFiltersClick = {
                        onAction(SearchResultAction.OpenFiltersSheet)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            RecipesDisplayModeSwitcher(
                modifier = Modifier.padding(horizontal = 24.dp),
                selectedMode = state.displayMode,
                onModeClick = {
                    onAction(SearchResultAction.ChangeDisplayMode(it))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (val recipesState = state.recipesState) {
                UiState.Idle, UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colors.accentColor)
                    }
                }

                is UiState.Error -> {
                    SearchResultErrorContent(
                        title = recipesState.title,
                        onRetryClick = {
                            onAction(SearchResultAction.Search)
                        }
                    )
                }

                is UiState.Success<List<RecipePreview>> -> {
                    if (recipesState.data.isEmpty()) {
                        EmptySearchResultContent()
                    } else {
                        RecipePreviewList(
                            recipes = recipesState.data,
                            displayMode = state.displayMode,
                            innerPadding = PaddingValues(),
                            onRecipeClick = { recipe ->
                                onAction(SearchResultAction.OpenRecipe(recipe.id))
                            }
                        )
                    }
                }
            }
        }

        if (state.isFiltersSheetVisible) {
            SearchFiltersBottomSheet(
                filters = state.draftFilters,
                ingredientGroupsState = state.ingredientGroupsState,
                onFiltersChange = {
                    onAction(SearchResultAction.ChangeDraftFilters(it))
                },
                onApplyClick = {
                    onAction(SearchResultAction.ApplyDraftFilters)
                },
                onResetClick = {
                    onAction(SearchResultAction.ResetDraftFilters)
                },
                onDismissRequest = {
                    onAction(SearchResultAction.CloseFiltersSheet)
                }
            )
        }
    }
}

@Composable
private fun SearchResultErrorContent(
    title: String,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = CustomTheme.colors.text,
            style = CustomTheme.typography.inter.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRetryClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomTheme.colors.accentColor,
                contentColor = CustomTheme.colors.background
            ),
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "Попробовать снова")
        }
    }
}

@Composable
private fun EmptySearchResultContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Ничего не найдено",
            style = CustomTheme.typography.inter.bodyMedium,
            color = CustomTheme.colors.secondaryText
        )
    }
}
