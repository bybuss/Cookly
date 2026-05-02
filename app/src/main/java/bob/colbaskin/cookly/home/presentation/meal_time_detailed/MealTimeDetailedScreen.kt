package bob.colbaskin.cookly.home.presentation.meal_time_detailed

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.components.PagerIndicator
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.components.SheetTopBar
import bob.colbaskin.cookly.common.components.feed_pagination.PaginationEffect
import bob.colbaskin.cookly.home.data.models.recipe_detailed.toDomainMealTime
import bob.colbaskin.cookly.home.domain.models.meal.MealFeedItem
import bob.colbaskin.cookly.home.presentation.components.paginatedItems
import bob.colbaskin.cookly.home.presentation.components.recommended_dish.RecommendationBanner
import bob.colbaskin.cookly.home.presentation.components.recommended_dish.RecommendedDish
import bob.colbaskin.cookly.navigation.Screens
import coil3.compose.AsyncImage
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.liquid
import io.github.fletchmckee.liquid.rememberLiquidState
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun MealTimeDetailedScreenRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MealTimeDetailedViewModel = hiltViewModel()
) {
    val mealTimeType: String =
        navController.currentBackStackEntry?.arguments?.getString("mealTimeType") ?: "unknown"
    val state = viewModel.state

    LaunchedEffect(mealTimeType) {
        viewModel.loadMealTimeFeed(mealTimeType)
    }

    MealTimeDetailedScreen(
        modifier = modifier,
        state = state,
        onAction = { action ->
            when (action) {
                MealTimeDetailedAction.NavigateBack -> navController.popBackStack()
                is MealTimeDetailedAction.NavigateToRecipeDetailed -> {
                    navController.navigate(Screens.RecipeDetailed(action.recipeId))
                }
                MealTimeDetailedAction.NavigateProfile -> navController.navigate(Screens.Profile)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MealTimeDetailedScreen(
    modifier: Modifier = Modifier,
    state: MealDetailedState,
    onAction: (MealTimeDetailedAction) -> Unit
) {
    val carouselItems = state.carouselItems
    val realPageCount = carouselItems.size
    val initialPage = remember(realPageCount) {
        if (realPageCount == 0) {
            0
        } else {
            val middle = Int.MAX_VALUE / 2
            middle - (middle % realPageCount)
        }
    }
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = {
            if (realPageCount == 0) 0 else Int.MAX_VALUE
        }
    )

    val liquidState = rememberLiquidState()

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val density = LocalDensity.current

        val peekHeight = (maxHeight * 0.40f).coerceIn(320.dp, 420.dp)
        val expandedHeight = (maxHeight * 0.90f).coerceIn(peekHeight + 120.dp, maxHeight - 24.dp)

        val collapsedTop = maxHeight - peekHeight
        val expandedTop = maxHeight - expandedHeight

        val collapsedTopPx = with(density) { collapsedTop.toPx() }
        val expandedTopPx = with(density) { expandedTop.toPx() }

        val sheetState = remember {
            AnchoredDraggableState(
                initialValue = MealTimeSheetValue.COLLAPSED
            )
        }

        val anchors = remember(collapsedTopPx, expandedTopPx) {
            DraggableAnchors {
                MealTimeSheetValue.EXPANDED at expandedTopPx
                MealTimeSheetValue.COLLAPSED at collapsedTopPx
            }
        }

        LaunchedEffect(anchors) {
            sheetState.updateAnchors(anchors)
        }

        LaunchedEffect(sheetState.currentValue) {
            onAction(
                MealTimeDetailedAction.OnSheetStateChanged(
                    isExpanded = sheetState.currentValue == MealTimeSheetValue.EXPANDED
                )
            )
        }

        LaunchedEffect(pagerState.settledPage, realPageCount) {
            if (realPageCount > 0) {
                onAction(
                    MealTimeDetailedAction.OnPagerPageSettled(
                        pagerState.settledPage % realPageCount
                    )
                )
            }
        }

        LaunchedEffect(state.isAutoScrollEnabled, realPageCount) {
            if (!state.isAutoScrollEnabled || realPageCount <= 1) return@LaunchedEffect
            while (true) {
                delay(3000)
                if (pagerState.isScrollInProgress) continue
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }

        val flingBehavior = AnchoredDraggableDefaults.flingBehavior(
            state = sheetState,
            positionalThreshold = { it * 0.5f },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        val sheetOverlap = 24.dp
        val overlayBottomGap = 20.dp
        val backgroundHeight = collapsedTop + sheetOverlap

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(backgroundHeight)
                .align(Alignment.TopStart)
        ) {
            MealPager(
                modifier = Modifier
                    .fillMaxSize()
                    .liquefiable(liquidState),
                carouselItems = state.carouselItems,
                pagerState = pagerState
            )
            MealPagerOverlay(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .liquid(liquidState) {
                        shape = RectangleShape
                        frost = 8.dp
                        refraction = 0f
                        curve = 0f
                        edge = 0f
                        saturation = 1f
                        contrast = 1f
                    }
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = overlayBottomGap + sheetOverlap,
                        top = 16.dp
                    ),
                carouselItems = state.carouselItems,
                pagerState = pagerState,
                onAction = onAction
            )
        }
        SheetTopBar(
            modifier = Modifier
                .padding(16.dp)
                .zIndex(3f),
            liquidBoxText = state.mealTimeType.toDomainMealTime(isPlural = true),
            onBackClick = { onAction(MealTimeDetailedAction.NavigateBack) },
            onAvatarClick = { onAction(MealTimeDetailedAction.NavigateProfile) },
            avatarUrl = state.avatarUrl,
            fallbackLetter = state.avatarLetter,
        )
        DraggableSheet(
            modifier = Modifier.zIndex(2f),
            anchoredState = sheetState,
            flingBehavior = flingBehavior,
            collapsedTopPxFallback = collapsedTopPx,
            state = state,
            onAction = onAction
        )
    }
}

@Composable
private fun MealPagerOverlay(
    modifier: Modifier = Modifier,
    carouselItems: List<MealFeedItem>,
    pagerState: PagerState,
    onAction: (MealTimeDetailedAction) -> Unit
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography

    if (carouselItems.isEmpty()) return
    val currentMeal = carouselItems[pagerState.currentPage % carouselItems.size]

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = currentMeal.title,
                    color = colors.invertedText,
                    style = typography.inter.headlineMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentMeal.description,
                    color = colors.invertedText,
                    style = typography.inter.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(20.dp))

                PagerIndicator(
                    currentPage = pagerState.currentPage % carouselItems.size,
                    pageCount = carouselItems.size,
                    selectedColor = CustomTheme.colors.accentColor,
                    unselectedColor = CustomTheme.colors.invertedText,
                    spacedByDp = 12.dp
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(colors.background.copy(alpha = 0.92f))
                    .clickable {
                        onAction(MealTimeDetailedAction.NavigateToRecipeDetailed(currentMeal.id))
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_up_right),
                    contentDescription = null,
                    tint = colors.text
                )
            }
        }
    }
}

@Composable
private fun MealPager(
    modifier: Modifier = Modifier,
    carouselItems: List<MealFeedItem>,
    pagerState: PagerState
) {
    if (carouselItems.isEmpty()) return

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) {
        val item = carouselItems[it % carouselItems.size]
        MealPagerItem(
            modifier = Modifier.fillMaxSize(),
            item = item
        )
    }
}

@Composable
private fun MealPagerItem(
    modifier: Modifier = Modifier,
    item: MealFeedItem
) {
    Box(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            model = item.imageUrl,
            fallback = painterResource(id = R.drawable.fallback_avatar),
            error = painterResource(id = R.drawable.fallback_avatar),
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DraggableSheet(
    modifier: Modifier = Modifier,
    anchoredState: AnchoredDraggableState<MealTimeSheetValue>,
    flingBehavior: FlingBehavior,
    collapsedTopPxFallback: Float,
    state: MealDetailedState,
    onAction: (MealTimeDetailedAction) -> Unit
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography
    val gridState = rememberLazyGridState()

    PaginationEffect(
        gridState = gridState,
        itemCount = state.pagination.items.size,
        appendState = state.pagination.appendState,
        isEndReached = state.pagination.isEndReached,
        enabled = state.pagination.loadState is UiState.Success,
        onLoadNext = { onAction(MealTimeDetailedAction.LoadNextPage) }
    )

    val offsetY =
        if (anchoredState.offset.isNaN()) collapsedTopPxFallback
        else anchoredState.requireOffset()

    Box(
        modifier = modifier
            .offset { IntOffset(0, offsetY.roundToInt()) }
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(colors.background)
            .anchoredDraggable(
                state = anchoredState,
                orientation = Orientation.Vertical,
                flingBehavior = flingBehavior
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        HorizontalDivider(
            modifier = Modifier
                .width(134.dp)
                .clip(CircleShape)
                .padding(top = 8.dp),
            thickness = 5.dp,
            color = colors.secondaryText.copy(alpha = 0.2f)
        )
        LazyVerticalGrid(
            state = gridState,
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                RecommendedDish(
                    modifier = Modifier,
                    title = "Блюдо от Шефа",
                    aiAvatar = R.drawable.cheif_ai_avatar,
                    recommendationCard = {
                        RecommendationBanner(
                            modifier = Modifier,
                            cardTitle = "Fried Shrimp",
                            recipeImageUrl = "",
                            rating = 4.8,
                            ratingAmount = 163,
                            minutes = 20,
                            kcal = 150,
                            isFlameIconRed = false,
                            border = false,
                            backgroundHexColor = "#B9480D",
                            isLeftCard = false,
                            onOpenClick = {}
                        )
                    }
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Рецепты",
                    color = colors.text,
                    style = typography.madeInfinity.headlineSmall,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Normal
                )
            }
            paginatedItems(
                state = state.pagination,
                onRetry = { onAction(MealTimeDetailedAction.Refresh) },
                onClick = { onAction(MealTimeDetailedAction.NavigateToRecipeDetailed(it)) },
                accentColor = colors.accentColor,
                textColor = colors.text,
                invertedTextColor = colors.invertedText,
                secondaryTextColor = colors.secondaryText,
                titleMedium = typography.inter.titleMedium,
                bodyMedium = typography.inter.bodyMedium
            )
            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}

@Preview
@Composable
private fun MealDetailedPreview() {
    UfoodTheme {
        MealTimeDetailedScreen(
            state = MealDetailedState(),
            onAction = {}
        )
    }
}
