package bob.colbaskin.cookly.navigation.graphs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import bob.colbaskin.cookly.agreement.presentation.AgreementScreenRoot
import bob.colbaskin.cookly.agreement.presentation.policy.PolicyScreenRoot
import bob.colbaskin.cookly.agreement.presentation.terms_of_use.TermsOfUseScreenRoot
import bob.colbaskin.cookly.auth.presentation.AuthScreenRoot
import bob.colbaskin.cookly.create_recipe.presentation.CreateRecipeScreenRoot
import bob.colbaskin.cookly.home.presentation.recipe_detailed.RecipeDetailedScreenRoot
import bob.colbaskin.cookly.home.presentation.main.HomeScreenRoot
import bob.colbaskin.cookly.home.presentation.meal_detailed.MealCategoryDetailedScreenRoot
import bob.colbaskin.cookly.navigation.Screens
import bob.colbaskin.cookly.onboarding_preferences.presentation.OnboardingScreenRoot
import bob.colbaskin.cookly.profile.presentation.ProfileScreenRoot

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
        composable<Screens.Home> { HomeScreenRoot(navController = navController) }
        composable<Screens.Cart> { MealCategoryDetailedScreenRoot(navController = navController) }
        composable<Screens.Chat> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Chat Screen")
            }
        }
        composable<Screens.Favourites> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Favourites Screen")
            }
        }
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
        startDestination = Screens.MealCategoryDetailed(-1)
    ) {
        composable<Screens.MealCategoryDetailed> {
            MealCategoryDetailedScreenRoot(navController = navController)
        }
        composable<Screens.RecipeDetailed> {
            RecipeDetailedScreenRoot(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
        composable<Screens.CreateRecipe> {
            CreateRecipeScreenRoot(
                navController = navController,
                snackbarHostState = snackbarHostState,
                onNavigateToSuccess = { navController.popBackStack() },
            )
        }
        composable<Screens.ApplicationsReview> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Applications Review Screen")
            }
        }
        composable<Screens.CookingHistory> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Cooking History Screen")
            }
        }
        composable<Screens.RecipeStatuses> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Recipe Statuses Screen")
            }
        }
    }
}
