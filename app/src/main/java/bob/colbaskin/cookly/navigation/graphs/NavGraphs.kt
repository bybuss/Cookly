package bob.colbaskin.cookly.navigation.graphs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import bob.colbaskin.cookly.common.user_prefs.data.models.AgreementConfig
import bob.colbaskin.cookly.common.user_prefs.data.models.AuthConfig
import bob.colbaskin.cookly.common.user_prefs.data.models.OnboardingConfig
import bob.colbaskin.cookly.navigation.Screens

fun NavGraphBuilder.agreementGraph(
    navController: NavHostController,
    agreementStatus: AgreementConfig
) {
    navigation<Graphs.Agreement> (
        startDestination = getAgreementStartDestination(status = agreementStatus)
    ) {
        composable<Screens.Agreement> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Agreement Screen")
            }
        }
        composable<Screens.Policy> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Policy Screen")
            }
        }
        composable<Screens.TermsOfUse> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "TermsOfUse Screen")
            }
        }
    }
}

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    authStatus: AuthConfig
) {
    navigation<Graphs.Auth>(
        startDestination = getAuthStartDestination(status = authStatus)
    ) {
        composable<Screens.WebViewAuth> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "WebViewAuth Screen")
            }
        }
    }
}

fun NavGraphBuilder.onboardingGraph(
    navController: NavHostController,
    onboardingConfig: OnboardingConfig
) {
    navigation<Graphs.Onboarding> (
        startDestination = getOnboardingStartDestination(status = onboardingConfig)
    ) {
        composable<Screens.Preferences> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Preferences Screen")
            }
        }
    }
}

fun NavGraphBuilder.mainGraph(
    navController: NavHostController
) {
    navigation<Graphs.Main> (
        startDestination = Screens.Home
    ) {
        composable<Screens.Home> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Home Screen")
            }
        }
        composable<Screens.Cart> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Cart Screen")
            }
        }
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
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Profile Screen")
            }
        }
    }
}

private fun getAgreementStartDestination(status: AgreementConfig) = when (status) {
    AgreementConfig.NOT_ACCEPTED -> Screens.Agreement
    AgreementConfig.ACCEPTED -> Screens.WebViewAuth
}

private fun getAuthStartDestination(status: AuthConfig) = when (status) {
    AuthConfig.AUTHENTICATED -> Screens.Preferences
    AuthConfig.NOT_AUTHENTICATED -> Screens.WebViewAuth
}

private fun getOnboardingStartDestination(status: OnboardingConfig) = when (status) {
    OnboardingConfig.COMPLETED -> Screens.Home
    else -> Screens.Preferences
}
