package bob.colbaskin.cookly.profile.presentation.recipe_statuses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.components.CooklyPullToRefreshBox
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipePreview
import bob.colbaskin.cookly.common.recipe_preview.presentation.RecipeListState
import bob.colbaskin.cookly.common.recipe_preview.presentation.RecipePreviewList
import bob.colbaskin.cookly.common.recipe_preview.presentation.RecipesDisplayModeSwitcher
import bob.colbaskin.cookly.navigation.Screens
import kotlinx.coroutines.launch

@Composable
fun RecipeStatusesScreenRoot(
    navController: NavHostController,
    viewModel: RecipeStatusesViewModel = hiltViewModel()
) {
    RecipeStatusesScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is RecipeStatusesAction.OpenRecipe -> {
                    navController.navigate(Screens.RecipeDetailed(action.recipeId))
                }

                else -> Unit
            }

            viewModel.onAction(action)
        },
        onBackClick = {
            navController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecipeStatusesScreen(
    state: RecipeStatusesState,
    onAction: (RecipeStatusesAction) -> Unit,
    onBackClick: () -> Unit
) {
    val tabs = RecipeStatusesTab.entries
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = tabs.indexOf(state.selectedTab).coerceAtLeast(0),
        pageCount = { tabs.size }
    )

    LaunchedEffect(pagerState.currentPage) {
        val tab = tabs[pagerState.currentPage]
        if (state.selectedTab != tab) {
            onAction(RecipeStatusesAction.SelectTab(tab))
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        containerColor = colors.background,
        contentColor = colors.text,
        topBar = {
            Column(
                modifier = Modifier.background(colors.background)
            ) {
                ProfileRecipesHeader(onBackClick = onBackClick)

                ScrollableTabRow(
                    selectedTabIndex = tabs.indexOf(state.selectedTab),
                    containerColor = colors.background,
                    contentColor = colors.accentColor,
                    edgePadding = 16.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(
                                tabPositions[tabs.indexOf(state.selectedTab)]
                            ),
                            color = colors.accentColor
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = state.selectedTab == tab,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                                onAction(RecipeStatusesAction.SelectTab(tab))
                            },
                            text = {
                                Text(
                                    text = tab.title,
                                    style = typography.inter.bodyMedium,
                                    fontWeight =
                                        if (state.selectedTab == tab) {
                                            FontWeight.Bold
                                        } else {
                                            FontWeight.Normal
                                        },
                                    color =
                                        if (state.selectedTab == tab) {
                                            colors.accentColor
                                        } else {
                                            colors.secondaryText
                                        }
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(innerPadding)
        ) { page ->
            val tab = tabs[page]
            val tabState = state.stateFor(tab)

            ProfileRecipesTabPage(
                tab = tab,
                state = tabState,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun ProfileRecipesHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.padding(
            start = 8.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 8.dp
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.size(40.dp),
            onClick = onBackClick
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_left),
                contentDescription = "Назад",
                tint = CustomTheme.colors.text
            )
        }

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "Мои рецепты",
            color = CustomTheme.colors.text,
            style = CustomTheme.typography.helvetica.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileRecipesTabPage(
    tab: RecipeStatusesTab,
    state: RecipeListState,
    onAction: (RecipeStatusesAction) -> Unit
) {
    val colors = CustomTheme.colors

    val isRefreshing = state.recipesState is UiState.Loading

    CooklyPullToRefreshBox(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        isRefreshing = isRefreshing,
        onRefresh = {
            onAction(RecipeStatusesAction.RefreshTab(tab))
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            RecipesDisplayModeSwitcher(
                modifier = Modifier.padding(horizontal = 24.dp),
                selectedMode = state.displayMode,
                onModeClick = { mode ->
                    onAction(
                        RecipeStatusesAction.ChangeDisplayMode(
                            tab = tab,
                            mode = mode
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when (val recipesState = state.recipesState) {
                    UiState.Idle, UiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = colors.accentColor)
                        }
                    }
                    is UiState.Error -> {
                        ProfileRecipesErrorContent(
                            title = recipesState.title,
                            onRetryClick = {
                                onAction(
                                    RecipeStatusesAction.LoadTab(
                                        tab = tab,
                                        forceRefresh = true
                                    )
                                )
                            }
                        )
                    }
                    is UiState.Success<List<RecipePreview>> -> {
                        if (recipesState.data.isEmpty()) {
                            ProfileRecipesEmptyContent()
                        } else {
                            RecipePreviewList(
                                recipes = recipesState.data,
                                displayMode = state.displayMode,
                                innerPadding = PaddingValues(),
                                onRecipeClick = { recipe ->
                                    onAction(
                                        RecipeStatusesAction.OpenRecipe(recipe.id)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileRecipesErrorContent(
    title: String,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = CustomTheme.typography.inter.titleMedium,
            color = CustomTheme.colors.text
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRetryClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomTheme.colors.accentColor,
                contentColor = CustomTheme.colors.invertedText
            ),
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "Попробовать снова")
        }
    }
}

@Composable
private fun ProfileRecipesEmptyContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp)
    ) {
        item {
            Box(
                modifier = Modifier.fillParentMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Пока здесь ничего нет",
                    style = CustomTheme.typography.inter.bodyMedium,
                    color = CustomTheme.colors.secondaryText
                )
            }
        }
    }
}
