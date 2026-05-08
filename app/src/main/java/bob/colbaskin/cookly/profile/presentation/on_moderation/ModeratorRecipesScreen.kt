package bob.colbaskin.cookly.profile.presentation.on_moderation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.common.recipe_preview.presentation.RecipeListScreen
import bob.colbaskin.cookly.navigation.Screens

@Composable
fun ModeratorRecipesScreenRoot(
    navController: NavHostController,
    viewModel: ModeratorRecipesViewModel = hiltViewModel()
) {
    RecipeListScreen(
        title = "На модерации",
        state = viewModel.state,
        onAction = viewModel::onAction,
        onBackClick = {
            navController.popBackStack()
        },
        onRecipeClick = { recipeId ->
            navController.navigate(
                Screens.RecipeDetailed(recipeId = recipeId, isModerationReview = true)
            )
        }
    )
}
