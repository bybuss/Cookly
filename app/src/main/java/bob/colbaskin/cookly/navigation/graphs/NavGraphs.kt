package bob.colbaskin.cookly.navigation.graphs

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import bob.colbaskin.cookly.agreement.presentation.AgreementScreenRoot
import bob.colbaskin.cookly.agreement.presentation.policy.PolicyScreenRoot
import bob.colbaskin.cookly.agreement.presentation.terms_of_use.TermsOfUseScreenRoot
import bob.colbaskin.cookly.auth.presentation.AuthScreenRoot
import bob.colbaskin.cookly.chat.presentation.ChatScreenRoot
import bob.colbaskin.cookly.create_recipe.presentation.CreateRecipeScreenRoot
import bob.colbaskin.cookly.favourite.presentation.FavoriteRecipesScreenRoot
import bob.colbaskin.cookly.home.presentation.cook_steps.CookStepsScreenRoot
import bob.colbaskin.cookly.home.presentation.recipe_detailed.RecipeDetailedScreenRoot
import bob.colbaskin.cookly.home.presentation.main.HomeScreenRoot
import bob.colbaskin.cookly.home.presentation.meal_time_detailed.MealTimeDetailedScreenRoot
import bob.colbaskin.cookly.navigation.Screens
import bob.colbaskin.cookly.onboarding_preferences.presentation.OnboardingScreenRoot
import bob.colbaskin.cookly.profile.presentation.cooking_history.CookingHistoryScreenRoot
import bob.colbaskin.cookly.profile.presentation.on_moderation.ModeratorRecipesScreenRoot
import bob.colbaskin.cookly.profile.presentation.profile.ProfileScreenRoot
import bob.colbaskin.cookly.profile.presentation.recipe_statuses.RecipeStatusesScreenRoot
import bob.colbaskin.cookly.search_result.presentation.SearchResultScreenRoot
import bob.colbaskin.cookly.shopping_cart.presentation.ShoppingCartScreenRoot

fun NavGraphBuilder.agreementGraph(
    navController: NavHostController
) {
    navigation<Graphs.Agreement> (
        startDestination = Screens.Agreement
    ) {
        composable<Screens.Agreement> { AgreementScreenRoot(navController = navController) }
        composable<Screens.Policy> { PolicyScreenRoot(navController = navController) }
        composable<Screens.TermsOfUse> { TermsOfUseScreenRoot(navController = navController) }
    }
}

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
) {
    navigation<Graphs.Auth>(
        startDestination = Screens.WebViewAuth
    ) {
        composable<Screens.WebViewAuth> { AuthScreenRoot(navController = navController) }
    }
}

fun NavGraphBuilder.onboardingGraph(
    navController: NavHostController
) {
    navigation<Graphs.Onboarding> (
        startDestination = Screens.Preferences
    ) {
        composable<Screens.Preferences> { OnboardingScreenRoot(navController = navController) }
    }
}

fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    navigation<Graphs.Main> (
        startDestination = Screens.Home
    ) {
        composable<Screens.Home> {
            HomeScreenRoot(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
        composable<Screens.ShoppingCart> { ShoppingCartScreenRoot() }
        composable<Screens.Chat> { ChatScreenRoot(navController = navController) }
        composable<Screens.Favourites> { FavoriteRecipesScreenRoot(navController = navController) }
        composable<Screens.Profile> {
            ProfileScreenRoot(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
    }
}
fun NavGraphBuilder.detailedGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    navigation<Graphs.Detailed>(
        startDestination = Screens.MealTimeDetailed("unknown")
    ) {
        composable<Screens.SearchResult> {
            SearchResultScreenRoot(navController = navController)
        }
        composable<Screens.MealTimeDetailed> {
            MealTimeDetailedScreenRoot(navController = navController)
        }
        composable<Screens.RecipeDetailed> {
            RecipeDetailedScreenRoot(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
        composable<Screens.CookSteps> {
            CookStepsScreenRoot(navController = navController)
        }
        composable<Screens.CreateRecipe> {
            CreateRecipeScreenRoot(
                navController = navController,
                snackbarHostState = snackbarHostState,
                onNavigateToSuccess = { navController.popBackStack() },
            )
        }
        composable<Screens.OnModeration> { ModeratorRecipesScreenRoot(navController = navController) }
        composable<Screens.CookingHistory> {
            CookingHistoryScreenRoot(navController = navController)
        }
        composable<Screens.RecipeStatuses> {
            RecipeStatusesScreenRoot(navController = navController)
        }
    }
}
