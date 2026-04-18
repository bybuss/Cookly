package bob.colbaskin.cookly.home.presentation.meal_detailed

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.components.PagerIndicator
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.home.domain.models.Meal
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
import bob.colbaskin.cookly.home.presentation.components.DishCard
import bob.colbaskin.cookly.home.presentation.components.SheetTopBar
import bob.colbaskin.cookly.home.presentation.components.recommended_dish.RecommendationBanner
import bob.colbaskin.cookly.home.presentation.components.recommended_dish.RecommendedDish
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.liquid
import io.github.fletchmckee.liquid.rememberLiquidState
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun MealCategoryDetailedScreenRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MealCategoryDetailedViewModel = hiltViewModel()
) {
    val mealCategoryId: Int
        = navController.currentBackStackEntry?.arguments?.getInt("mealCategoryId") ?: -1
    val state = viewModel.state

    MealCategoryDetailedScreen(
        modifier = modifier,
        state = state,
        onAction = { action ->
            when (action) {
                MealCategoryDetailedAction.NavigateBack -> navController.popBackStack()
                is MealCategoryDetailedAction.NavigateToMealRecipe -> { /*navController.navigate(...)*/ }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MealCategoryDetailedScreen(
    modifier: Modifier = Modifier,
    state: MealDetailedState,
    onAction: (MealCategoryDetailedAction) -> Unit
) {
    val mealsList = state.mealsList
    val realPageCount = mealsList.size
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
                initialValue = MealCategorySheetValue.COLLAPSED
            )
        }

        val anchors = remember(collapsedTopPx, expandedTopPx) {
            DraggableAnchors {
                MealCategorySheetValue.EXPANDED at expandedTopPx
                MealCategorySheetValue.COLLAPSED at collapsedTopPx
            }
        }

        LaunchedEffect(anchors) {
            sheetState.updateAnchors(anchors)
        }

        LaunchedEffect(sheetState.currentValue) {
            onAction(
                MealCategoryDetailedAction.OnSheetStateChanged(
                    isExpanded = sheetState.currentValue == MealCategorySheetValue.EXPANDED
                )
            )
        }

        LaunchedEffect(pagerState.settledPage, realPageCount) {
            if (realPageCount > 0) {
                onAction(
                    MealCategoryDetailedAction.OnPagerPageSettled(
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
                mealsList = mealsList,
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
                mealsList = mealsList,
                pagerState = pagerState,
                onAction = onAction
            )
        }
        SheetTopBar(
            modifier = Modifier
                .padding(16.dp)
                .zIndex(3f),
            liquidBoxText = stringResource(state.mealType.displayNameId),
            onBackClick = { onAction(MealCategoryDetailedAction.NavigateBack) },
            avatarId = R.drawable.user_avatar_mock
        )
        DraggableSheet(
            modifier = Modifier.zIndex(2f),
            state = sheetState,
            flingBehavior = flingBehavior,
            collapsedTopPxFallback = collapsedTopPx
        )
    }
}

@Composable
private fun MealPagerOverlay(
    modifier: Modifier = Modifier,
    mealsList: List<Meal>,
    pagerState: PagerState,
    onAction: (MealCategoryDetailedAction) -> Unit
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography

    if (mealsList.isEmpty()) return
    val currentMeal = mealsList[pagerState.currentPage % mealsList.size]

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
                    currentPage = pagerState.currentPage % mealsList.size,
                    pageCount = mealsList.size,
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
                        onAction(MealCategoryDetailedAction.NavigateToMealRecipe(currentMeal.id))
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
    mealsList: List<Meal>,
    pagerState: PagerState
) {
    if (mealsList.isEmpty()) return

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) {
        val item = mealsList[it % mealsList.size]
        MealPagerItem(
            modifier = Modifier.fillMaxSize(),
            item = item
        )
    }
}

@Composable
private fun MealPagerItem(
    modifier: Modifier = Modifier,
    item: Meal
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = item.imageId),
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
    state: AnchoredDraggableState<MealCategorySheetValue>,
    flingBehavior: FlingBehavior,
    collapsedTopPxFallback: Float
) {
    val colors = CustomTheme.colors

    val offsetY = if (state.offset.isNaN()) collapsedTopPxFallback else state.requireOffset()

    Box(
        modifier = modifier
            .offset { IntOffset(0, offsetY.roundToInt()) }
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(colors.background)
            .anchoredDraggable(
                state = state,
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
            color =  colors.secondaryText.copy(alpha = 0.2f)
        )
        LazyVerticalGrid(
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
                            backgroundImage = R.drawable.shrimp_soup_image,
                            rating = 4.8,
                            ratingAmount = 163,
                            minutes = 20,
                            kcal = 150,
                            isFlameIconRed = false,
                            border = false,
                            backgroundHexColor = "#B9480D",
                            isLeftCard = false
                        )
                    }
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Рецепты",
                    color = CustomTheme.colors.text,
                    style = CustomTheme.typography.madeInfinity.headlineSmall,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Normal
                )
            }
            items(20) {
                DishCard(
                    modifier = Modifier,
                    title = "Fried Shrimp",
                    minutes = 20,
                    dishImage = R.drawable.fried_egg_backgroiund,
                    rating = 4.8,
                    ratingAmount = 168,
                    kcal = 150,
                    isFlameIconRed = false,
                )
            }
        }
    }
}

@Preview
@Composable
private fun MealDetailedPreview() {
    UfoodTheme {
        MealCategoryDetailedScreen(
            state = MealDetailedState(),
            onAction = {}
        )
    }
}
