package bob.colbaskin.cookly.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state

    HomeScreen(
        modifier = modifier,
        state = state,
        onAction = { action ->
            when (action) {
                is HomeAction.TestAction -> {}
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {

}
