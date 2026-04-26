package bob.colbaskin.cookly.navigation.graphs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import bob.colbaskin.cookly.agreement.presentation.AgreementScreenRoot
import bob.colbaskin.cookly.agreement.presentation.policy.PolicyScreenRoot
import bob.colbaskin.cookly.agreement.presentation.terms_of_use.TermsOfUseScreenRoot
import bob.colbaskin.cookly.auth.presentation.AuthScreenRoot
import bob.colbaskin.cookly.create_recipe.presentation.CreateRecipeScreenRoot
import bob.colbaskin.cookly.home.presentation.dish_detailed.DishDetailedScreenRoot
import bob.colbaskin.cookly.home.presentation.main.HomeScreenRoot
import bob.colbaskin.cookly.home.presentation.meal_detailed.MealCategoryDetailedScreenRoot
import bob.colbaskin.cookly.navigation.Screens
import bob.colbaskin.cookly.onboarding_preferences.presentation.OnboardingScreenRoot

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
            CreateRecipeScreenRoot(
                navController = navController,
                snackbarHostState = snackbarHostState,
                onNavigateToSuccess = { navController.popBackStack() },
            )
        }
        composable<Screens.Favourites> { DishDetailedScreenRoot(navController = navController) }
        composable<Screens.Profile> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Profile Screen")
            }
        }
    }
}

fun NavGraphBuilder.detailedGraph(
    navController: NavHostController
) {
    navigation<Graphs.Detailed>(
        startDestination = Screens.MealCategoryDetailed(-1)
    ) {
        composable<Screens.MealCategoryDetailed> { MealCategoryDetailedScreenRoot(navController = navController) }
        composable<Screens.DishDetailed> { DishDetailedScreenRoot(navController = navController) }
    }
}
