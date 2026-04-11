package bob.colbaskin.cookly.navigation.graphs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import bob.colbaskin.cookly.agreement.presentation.AgreementScreenRoot
import bob.colbaskin.cookly.agreement.presentation.policy.PolicyScreenRoot
import bob.colbaskin.cookly.agreement.presentation.terms_of_use.TermsOfUseScreenRoot
import bob.colbaskin.cookly.auth.presentation.AuthScreenRoot
import bob.colbaskin.cookly.home.presentation.main.HomeScreenRoot
import bob.colbaskin.cookly.home.presentation.discovery.FeedScreenRoot
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
    navController: NavHostController
) {
    navigation<Graphs.Main> (
        startDestination = Screens.Home
    ) {
        composable<Screens.Home> { HomeScreenRoot(navController = navController) }
        composable<Screens.Cart> { FeedScreenRoot(navController = navController) }
        composable<Screens.Chat> {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Red), contentAlignment = Alignment.Center) {
                Text(text = "Chat Screen")
            }
        }
        composable<Screens.Favourites> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Favourites Screen")
            }
        }
        composable<Screens.Profile> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Profile Screen")
            }
        }
    }
}

fun NavGraphBuilder.detailedGraph(
    navController: NavController
) {
    navigation<Graphs.Detailed>(
        startDestination = Screens.CategoryDetails(-1)
    ) {
        composable<Screens.CategoryDetails> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "CategoryDetails Screen, id=$id")
            }
        }
        composable<Screens.DishCategory> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "DishCategory Screen, id=$id")
            }
        }
    }
}
