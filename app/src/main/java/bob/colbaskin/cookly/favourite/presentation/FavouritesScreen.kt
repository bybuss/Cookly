package bob.colbaskin.cookly.favourite.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.common.recipe_preview.presentation.RecipeListScreen
import bob.colbaskin.cookly.navigation.Screens

@Composable
fun FavoriteRecipesScreenRoot(
    navController: NavHostController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    RecipeListScreen(
        state = viewModel.state,
        onAction = viewModel::onAction,
        onRecipeClick = { recipeId ->
            navController.navigate(Screens.RecipeDetailed(recipeId))
        }
    )
}
