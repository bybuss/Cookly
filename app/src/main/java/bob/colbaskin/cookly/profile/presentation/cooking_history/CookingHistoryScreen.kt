package bob.colbaskin.cookly.profile.presentation.cooking_history

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.common.recipe_preview.presentation.RecipeListScreen
import bob.colbaskin.cookly.navigation.Screens

@Composable
fun CookingHistoryScreenRoot(
    navController: NavHostController,
    viewModel: CookingHistoryViewModel = hiltViewModel()
) {
    RecipeListScreen(
        title = "История приготовленных блюд",
        state = viewModel.state,
        onAction = viewModel::onAction,
        onBackClick = { navController.popBackStack() },
        onRecipeClick = { recipeId ->
            navController.navigate(Screens.RecipeDetailed(recipeId))
        }
    )
}
