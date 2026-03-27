package bob.colbaskin.cookly.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.user_prefs.data.models.AgreementConfig
import bob.colbaskin.cookly.common.user_prefs.data.models.AuthConfig
import bob.colbaskin.cookly.common.user_prefs.data.models.UserPreferences
import bob.colbaskin.cookly.navigation.bottom_bar.BottomNavBar
import bob.colbaskin.cookly.navigation.graphs.Graphs
import bob.colbaskin.cookly.navigation.graphs.agreementGraph
import bob.colbaskin.cookly.navigation.graphs.authGraph
import bob.colbaskin.cookly.navigation.graphs.detailedGraph
import bob.colbaskin.cookly.navigation.graphs.mainGraph
import bob.colbaskin.cookly.navigation.graphs.onboardingGraph

@Composable
fun AppNavHost(uiState: UiState.Success<UserPreferences>) {

    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentGraph = currentDestination?.parent?.route
    val isBottomBarVisible = currentGraph == Graphs.Main::class.qualifiedName

    Scaffold(
        bottomBar = {
            AnimatedVisibility(visible = isBottomBarVisible) {
                BottomNavBar(navController = navController)
            }
        },
        containerColor = CustomTheme.colors.background,
        contentColor = CustomTheme.colors.text
    ) { innerPadding ->
        NavHost(
            startDestination = getStartDestination(
                agreementStatus = uiState.data.agreementStatus,
                authStatus = uiState.data.authStatus,
            ),
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        ) {
            agreementGraph(navController = navController)
            authGraph(navController = navController)
            onboardingGraph(navController = navController)
            mainGraph(navController = navController)
            detailedGraph(navController = navController)
        }
    }
}

private fun getStartDestination(
    agreementStatus: AgreementConfig,
    authStatus: AuthConfig
) =
    when (agreementStatus) {
        AgreementConfig.ACCEPTED -> when (authStatus) {
            AuthConfig.AUTHENTICATED -> Graphs.Onboarding
            AuthConfig.NOT_AUTHENTICATED -> Graphs.Onboarding
        }
        AgreementConfig.NOT_ACCEPTED -> Graphs.Agreement
    }
