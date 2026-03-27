package bob.colbaskin.cookly.onboarding_preferences.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.navigation.graphs.Graphs
import bob.colbaskin.cookly.onboarding_preferences.presentation.pages.AllergyChoicePage
import bob.colbaskin.cookly.onboarding_preferences.presentation.pages.DietChoicePage

const val PAGE_COUNT = 2

@Composable
fun OnboardingScreenRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val effect = viewModel.effect.collectAsState(initial = null).value
    val pagerState = rememberPagerState(
        initialPage = state.currentPageIndex,
        pageCount = { PAGE_COUNT }
    )

    LaunchedEffect(effect) {
        when (effect) {
            OnboardingEffect.CompleteOnboarding -> navController.navigate(Graphs.Main) {
                popUpTo(Graphs.Onboarding) { inclusive = true }
            }
            is OnboardingEffect.ScrollToPage -> pagerState.animateScrollToPage(effect.page)
            null -> {}
        }
    }

    OnboardingScreen(
        modifier = modifier.padding(16.dp),
        state = state,
        onAction = viewModel::onAction,
        pagerState = pagerState
    )
}

@Composable
private fun OnboardingScreen(
    modifier: Modifier = Modifier,
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
    pagerState: PagerState,
) {
    PagerWithIndicator(
        pageCount = PAGE_COUNT,
        modifier = modifier,
        pagerState = pagerState,
        content = { pageIndex ->
            when (pageIndex) {
                0 -> DietChoicePage(
                    selectedDiets = state.selectedDiets,
                    onToggleDiet = { onAction(OnboardingAction.ToggleDiet(it)) }
                )
                1 -> AllergyChoicePage(
                    selectedAllergies = state.selectedAllergies,
                    onToggleAllergy = { onAction(OnboardingAction.ToggleAllergy(it)) }
                )
            }
        },
        onAction = onAction
    )
}
