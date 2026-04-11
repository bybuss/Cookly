package bob.colbaskin.cookly.home.presentation.meal_detailed

import androidx.annotation.DrawableRes
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
import bob.colbaskin.cookly.home.presentation.components.LiquidBox
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.liquid
import io.github.fletchmckee.liquid.rememberLiquidState
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MealDetailedScreen(
    modifier: Modifier = Modifier,
    state: MealDetailedState,
    onAction: (MealDetailedAction) -> Unit
) {
    val mealsList = state.mealsList
    val pagerState = rememberPagerState(pageCount = { mealsList.size })
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
                initialValue = MealSheetValue.COLLAPSED
            )
        }

        val anchors = remember(collapsedTopPx, expandedTopPx) {
            DraggableAnchors {
                MealSheetValue.EXPANDED at expandedTopPx
                MealSheetValue.COLLAPSED at collapsedTopPx
            }
        }

        LaunchedEffect(anchors) {
            sheetState.updateAnchors(anchors)
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
        MealTopBar(
            modifier = Modifier
                .padding(16.dp)
                .zIndex(3f),
            liquidBoxText = stringResource(state.mealType.displayNameId),
            onAction = onAction,
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
private fun MealTopBar(
    modifier: Modifier = Modifier,
    liquidBoxText: String,
    onAction: (MealDetailedAction) -> Unit,
    @DrawableRes avatarId: Int // TODO: заменить потом на ссыллку
) {
    val colors = CustomTheme.colors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = { onAction(MealDetailedAction.NavigateBack) }),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(-(1).dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colors.background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_left),
                    contentDescription = null,
                    tint = colors.text
                )
            }
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .clip(CircleShape)
                    .background(colors.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    text = "На главную",
                    style = CustomTheme.typography.inter.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = colors.text
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        LiquidBox(
            modifier = Modifier,
            text = liquidBoxText
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(avatarId),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
private fun MealPagerOverlay(
    modifier: Modifier = Modifier,
    mealsList: List<Meal>,
    pagerState: PagerState,
    onAction: (MealDetailedAction) -> Unit
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography
    val currentMeal = mealsList.getOrNull(pagerState.currentPage) ?: return

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
                    currentPage = pagerState.currentPage,
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
                        onAction(MealDetailedAction.NavigateToMealRecipe(currentMeal.id))
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
    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) {
        val item = mealsList[it]
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
    state: AnchoredDraggableState<MealSheetValue>,
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
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally)
                    .width(40.dp)
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(colors.secondaryCardBackground)
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Draggable content",
                    style = CustomTheme.typography.inter.bodyMedium,
                    color = colors.text
                )
            }
        }
    }
}

@Preview
@Composable
private fun MealDetailedPreview() {
    UfoodTheme {
        MealDetailedScreen(
            state = MealDetailedState(),
            onAction = {}
        )
    }
}
