@file:OptIn(ExperimentalFoundationApi::class)
package bob.colbaskin.cookly.home.presentation.discovery

import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.home.domain.models.MealType
import bob.colbaskin.cookly.home.domain.models.QuickCategoryType

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.home.presentation.components.DishCard
import bob.colbaskin.cookly.home.presentation.components.recommended_dish.RecommendationBanner
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowBack
import compose.icons.tablericons.ArrowDown
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

sealed interface FeedAction {
    data object NavigateBack : FeedAction
    data object HeroCtaClick : FeedAction
    data object ProfileClick : FeedAction
    data class HeroPageChanged(val page: Int) : FeedAction
}

data class HeroDishUi(
    val id: String,
    @DrawableRes val imageRes: Int,
    val title: String,
    val subtitle: String,
    val category: String
)

data class FeedState(
    val heroSlides: List<HeroDishUi> = listOf(
        HeroDishUi(
            id = "1",
            imageRes = R.drawable.fried_egg_backgroiund,
            title = "Яичница\n“Мятые Иички”",
            subtitle = "Не густо не пусто",
            category = "Завтраки"
        ),
        HeroDishUi(
            id = "2",
            imageRes = R.drawable.shrimp_soup_image,
            title = "Fried Shrimp\nDeluxe",
            subtitle = "Сытный и яркий вкус",
            category = "Завтраки"
        ),
        HeroDishUi(
            id = "3",
            imageRes = R.drawable.fried_egg_backgroiund,
            title = "Тост с яйцом\nи хрустящей корочкой",
            subtitle = "Быстро, вкусно, просто",
            category = "Завтраки"
        )
    ),
    val quickCardsList: List<QuickCategoryType> = listOf(
        QuickCategoryType.QUICK_COOK,
        QuickCategoryType.DIETARY,
        QuickCategoryType.HIGH_CALORIE,
        QuickCategoryType.ON_TREND,
    ),
    val mealsList: List<MealType> = listOf(
        MealType.BREAKFAST,
        MealType.LUNCH,
        MealType.DINNER,
        MealType.DINNER,
    )
)

@HiltViewModel
class FeedViewModel @Inject constructor() : ViewModel() {
    var state by mutableStateOf(FeedState())
        private set

    fun onAction(action: FeedAction) {
        when (action) {
            FeedAction.NavigateBack -> Unit
            FeedAction.HeroCtaClick -> Unit
            FeedAction.ProfileClick -> Unit
            is FeedAction.HeroPageChanged -> Unit
        }
    }
}

@Composable
fun FeedScreenRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val state = viewModel.state

    FeedScreen(
        modifier = modifier,
        state = state,
        onAction = { action ->
            when (action) {
                FeedAction.NavigateBack -> navController.popBackStack()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

private enum class SheetValue {
    Collapsed,
    Expanded
}

@Composable
private fun FeedScreen(
    modifier: Modifier = Modifier,
    state: FeedState,
    onAction: (FeedAction) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { state.heroSlides.size })
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CustomTheme.colors.background,
        contentColor = CustomTheme.colors.text,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CustomTheme.colors.background)
        ) {
            val density = LocalDensity.current
            val screenHeight = maxHeight
            val heroHeight = screenHeight * 0.42f
            val overlap = 34.dp

            val expandedOffsetPx = 0f
            val collapsedOffsetPx = with(density) { (heroHeight - overlap).toPx() }

            val sheetOffset = remember { Animatable(collapsedOffsetPx) }

            var currentSheetValue by remember {
                mutableStateOf(SheetValue.Collapsed)
            }

            val isGridAtTop = gridState.firstVisibleItemIndex == 0 &&
                    gridState.firstVisibleItemScrollOffset == 0

            suspend fun expandSheet() {
                sheetOffset.animateTo(
                    targetValue = expandedOffsetPx,
                    animationSpec = tween(durationMillis = 260)
                )
                currentSheetValue = SheetValue.Expanded
            }

            suspend fun collapseSheet() {
                sheetOffset.animateTo(
                    targetValue = collapsedOffsetPx,
                    animationSpec = tween(durationMillis = 260)
                )
                currentSheetValue = SheetValue.Collapsed
            }

            val collapseProgress = ((collapsedOffsetPx - sheetOffset.value) / collapsedOffsetPx)
                .coerceIn(0f, 1f)

            val heroAlpha = 1f - collapseProgress * 0.2f
            val heroTranslateY = with(density) { (-18.dp).toPx() } * collapseProgress

            Box(modifier = Modifier.fillMaxSize()) {
                HeroPagerBlock(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heroHeight)
                        .graphicsLayer {
                            translationY = heroTranslateY
                        }
                        .alpha(heroAlpha),
                    pagerState = pagerState,
                    slides = state.heroSlides,
                    onAction = onAction
                )

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset { IntOffset(0, sheetOffset.value.roundToInt()) }
                        .pointerInput(currentSheetValue, isGridAtTop) {
                            detectVerticalDragGestures(
                                onVerticalDrag = { change, dragAmount ->
                                    change.consume()

                                    val shouldDragSheet =
                                        currentSheetValue == SheetValue.Collapsed ||
                                                (currentSheetValue == SheetValue.Expanded && isGridAtTop)

                                    if (!shouldDragSheet) return@detectVerticalDragGestures

                                    val newOffset = (sheetOffset.value + dragAmount)
                                        .coerceIn(expandedOffsetPx, collapsedOffsetPx)

                                    scope.launch {
                                        sheetOffset.snapTo(newOffset)
                                    }
                                },
                                onDragEnd = {
                                    scope.launch {
                                        val middle = collapsedOffsetPx / 2f
                                        if (sheetOffset.value < middle) {
                                            expandSheet()
                                        } else {
                                            collapseSheet()
                                        }
                                    }
                                }
                            )
                        },
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                    color = CustomTheme.colors.background,
                    shadowElevation = 6.dp,
                    tonalElevation = 0.dp
                ) {
                    FeedSheetContent(
                        state = state,
                        gridState = gridState,
                        userScrollEnabled = currentSheetValue == SheetValue.Expanded,
                        topSpacing = overlap + 12.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroPagerBlock(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    slides: List<HeroDishUi>,
    onAction: (FeedAction) -> Unit
) {
    val statusPadding = WindowInsets.statusBars.asPaddingValues()
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(isDragged, slides.size) {
        if (slides.size <= 1) return@LaunchedEffect

        while (!isDragged) {
            delay(3_000)
            val next = (pagerState.currentPage + 1) % slides.size
            pagerState.animateScrollToPage(next)
            onAction(FeedAction.HeroPageChanged(next))
        }
    }

    Box(modifier = modifier) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState
        ) { page ->
            val item = slides[page]

            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(item.imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                listOf(
                                    androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.18f),
                                    androidx.compose.ui.graphics.Color.Transparent,
                                    androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.34f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = statusPadding.calculateTopPadding() + 12.dp)
                        .padding(horizontal = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HeroCircleButton(
                            onClick = { onAction(FeedAction.NavigateBack) }
                        ) {
                            Icon(
                                imageVector = TablerIcons.ArrowBack,
                                contentDescription = null,
                                tint = CustomTheme.colors.text
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        HeroChip(text = "На главную", selected = false)

                        Spacer(modifier = Modifier.width(8.dp))

                        HeroChip(text = item.category, selected = true)

                        Spacer(modifier = Modifier.weight(1f))

                        androidx.compose.foundation.Image(
                            painter = painterResource(R.drawable.cheif_ai_avatar),
                            contentDescription = null,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = item.title,
                        color = androidx.compose.ui.graphics.Color.White,
                        style = CustomTheme.typography.madeInfinity.headlineSmall,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 34.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.subtitle,
                        color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.88f),
                        style = CustomTheme.typography.inter.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PagerLineIndicator(
                            modifier = Modifier.weight(1f),
                            currentPage = pagerState.currentPage,
                            pageCount = slides.size
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        HeroCircleButton(
                            onClick = { onAction(FeedAction.HeroCtaClick) },
                            containerColor = androidx.compose.ui.graphics.Color.White
                        ) {
                            Icon(
                                imageVector = TablerIcons.ArrowDown,
                                contentDescription = null,
                                tint = androidx.compose.ui.graphics.Color.Black
                            )

                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun FeedSheetContent(
    state: FeedState,
    gridState: LazyGridState,
    userScrollEnabled: Boolean,
    topSpacing: androidx.compose.ui.unit.Dp
) {
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    LazyVerticalGrid(
        state = gridState,
        userScrollEnabled = userScrollEnabled,
        modifier = Modifier
            .fillMaxSize()
            .background(CustomTheme.colors.background)
            .padding(horizontal = 16.dp),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            top = topSpacing,
            bottom = bottomPadding + 16.dp
        )
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = "Завтрак от Шефа",
                color = CustomTheme.colors.text,
                style = CustomTheme.typography.madeInfinity.headlineSmall,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
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
                backgroundHexColor = "#D8C7A8",
                isLeftCard = false
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

        items(20) { index ->
            DishCard(
                modifier = Modifier,
                title = "Fried Shrimp",
                minutes = 20,
                dishImage = if (index % 2 == 0) {
                    R.drawable.fried_egg_backgroiund
                } else {
                    R.drawable.shrimp_soup_image
                },
                rating = 4.8,
                ratingAmount = 168,
                kcal = 150,
                isFlameIconRed = false,
            )
        }
    }
}

@Composable
private fun PagerLineIndicator(
    modifier: Modifier = Modifier,
    currentPage: Int,
    pageCount: Int
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { page ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(
                        if (page == currentPage) {
                            CustomTheme.colors.secondAccentColor
                        } else {
                            androidx.compose.ui.graphics.Color.White.copy(alpha = 0.55f)
                        }
                    )
            )
        }
    }
}

@Composable
private fun HeroChip(
    text: String,
    selected: Boolean
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(
                if (selected) {
                    androidx.compose.ui.graphics.Color.White.copy(alpha = 0.22f)
                } else {
                    androidx.compose.ui.graphics.Color.White.copy(alpha = 0.94f)
                }
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            color = if (selected) {
                androidx.compose.ui.graphics.Color.White
            } else {
                CustomTheme.colors.text
            },
            style = CustomTheme.typography.inter.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun HeroCircleButton(
    onClick: () -> Unit,
    containerColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.94f),
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = containerColor,
        modifier = Modifier.size(38.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
            content = content
        )
    }
}
