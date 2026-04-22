package bob.colbaskin.cookly.home.presentation.dish_detailed

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import kotlin.math.roundToInt
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.home.domain.models.Allergen
import bob.colbaskin.cookly.home.domain.models.StartCookSwipeAnchor
import bob.colbaskin.cookly.home.presentation.components.SheetTopBar

@Composable
fun DishDetailedScreenRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: DishDetailedViewModel = hiltViewModel()
) {
    val state = viewModel.state

    DishDetailedScreen(
        modifier = modifier,
        state = state,
        onAction = { action ->
            when (action) {
                DishDetailedAction.NavigateBack -> navController.popBackStack()
                DishDetailedAction.StartCook -> navController.popBackStack() // FIXME: изменить потом на навигацию на шаги готовки рецепта жи ес
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun DishDetailedScreen(
    modifier: Modifier = Modifier,
    state: DishDetailedState,
    onAction: (DishDetailedAction) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F3EE))
    ) {
        val density = LocalDensity.current

        val avatarSize = (maxWidth * 0.54f).coerceIn(180.dp, 240.dp)
        val expandedTop = (maxHeight * 0.145f).coerceIn(96.dp, 96.dp)

        val collapsedVisibleHeight = (maxHeight * 0.42f).coerceIn(476.dp, 770.dp)
        val collapsedTop = maxHeight - collapsedVisibleHeight

        val expandedTopPx = with(density) { expandedTop.toPx() }
        val collapsedTopPx = with(density) { collapsedTop.toPx() }

        val sheetState = remember {
            AnchoredDraggableState(
                initialValue = DishSheetValue.COLLAPSED
            )
        }

        val anchors = remember(expandedTopPx, collapsedTopPx) {
            DraggableAnchors {
                DishSheetValue.EXPANDED at expandedTopPx
                DishSheetValue.COLLAPSED at collapsedTopPx
            }
        }

        LaunchedEffect(anchors) {
            sheetState.updateAnchors(anchors)
        }

        val flingBehavior = AnchoredDraggableDefaults.flingBehavior(
            state = sheetState,
            positionalThreshold = { distance -> distance * 0.5f },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )

        DishBackground(
            modifier = Modifier
                .fillMaxWidth()
                .height(collapsedTop + avatarSize / 2 + 24.dp)
                .align(Alignment.TopStart),
            dishName = state.dishName,
            dishImageRes = state.dishAvatar
        )
        DishSheet(
            modifier = Modifier.zIndex(3f),
            onAction = onAction,
            state = state,
            anchoredState = sheetState,
            flingBehavior = flingBehavior,
            expandedTop = expandedTop,
            expandedTopPx = expandedTopPx,
            collapsedTopPxFallback = collapsedTopPx,
            avatarSize = avatarSize,
            dishAvatarId = state.dishAvatar,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .zIndex(3f),
            horizontalAlignment = Alignment.End
        ) {
            SheetTopBar(
                modifier = Modifier,
                liquidBoxText = state.mealType,
                onBackClick = { onAction(DishDetailedAction.NavigateBack) },
                avatarId = R.drawable.user_avatar_mock
            )
            Spacer(modifier = Modifier.height(16.dp))
            HeartBubble(
                modifier = Modifier,
                isLiked = state.isRecipeLiked,
                onClick = { onAction(DishDetailedAction.ToggleLike) }
            )
        }
    }
}

@Composable
private fun DishBackground(
    modifier: Modifier = Modifier,
    dishName: String,
    @DrawableRes dishImageRes: Int
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = dishImageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .blur(16.dp)
        )
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 100.dp),
            text = dishName,
            textAlign = TextAlign.Center,
            style = CustomTheme.typography.inter.headlineMedium,
            fontWeight = FontWeight.Medium,
            color = CustomTheme.colors.invertedText
        )
    }
}

@Composable
private fun CartBubble(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = CustomTheme.colors

    Box(
        modifier = modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(colors.accentSecondSurface)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.add_to_cart),
            contentDescription = null,
            tint = colors.addToCartColor
        )
    }
}

@Composable
private fun HeartBubble(
    modifier: Modifier = Modifier,
    isLiked: Boolean,
    onClick: () -> Unit
) {
    val colors = CustomTheme.colors

    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(colors.background)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.heart_ic),
            contentDescription = null,
            tint = if (isLiked) colors.likeColor else colors.outlinedStatsSurface
        )
    }
}

@Composable
private fun DishAvatar(
    modifier: Modifier = Modifier,
    @DrawableRes dishAvatarId: Int
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = CircleShape,
                clip = false
            )
            .clip(CircleShape)
    ) {
        Image(
            painter = painterResource(id = dishAvatarId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
        )
    }
}

@Composable
private fun StartCookSwipeButton(
    modifier: Modifier = Modifier,
    onSwiped: () -> Unit
) {
    val density = LocalDensity.current

    val thumbSize = 62.dp
    val horizontalPadding = 8.dp
    val shape = RoundedCornerShape(999.dp)

    var containerWidthPx by remember { mutableIntStateOf(0) }
    val thumbSizePx = with(density) { thumbSize.roundToPx() }
    val horizontalPaddingPx = with(density) { horizontalPadding.roundToPx() }

    val maxDragPx = remember(containerWidthPx, thumbSizePx, horizontalPaddingPx) {
        (containerWidthPx - thumbSizePx - horizontalPaddingPx * 2).coerceAtLeast(0)
    }

    val anchoredState = remember {
        AnchoredDraggableState(
            initialValue = StartCookSwipeAnchor.Start
        )
    }

    val anchors = remember(maxDragPx) {
        DraggableAnchors {
            StartCookSwipeAnchor.Start at 0f
            StartCookSwipeAnchor.End at maxDragPx.toFloat()
        }
    }

    LaunchedEffect(anchors) {
        anchoredState.updateAnchors(anchors)
    }

    LaunchedEffect(anchoredState.currentValue) {
        if (anchoredState.currentValue == StartCookSwipeAnchor.End) {
            onSwiped()
            anchoredState.animateTo(StartCookSwipeAnchor.Start)
        }
    }

    val flingBehavior = AnchoredDraggableDefaults.flingBehavior(
        state = anchoredState,
        positionalThreshold = { distance -> distance * 0.5f },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val rawOffset = if (anchoredState.offset.isNaN()) 0f else anchoredState.requireOffset()
    val thumbOffset = rawOffset.coerceIn(0f, maxDragPx.toFloat())

    Box(
        modifier = modifier
            .height(78.dp)
            .clip(shape)
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = 0.10f),
                shape = shape
            )
            .onSizeChanged { containerWidthPx = it.width }
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(
                    fraction = ((thumbOffset + thumbSizePx + horizontalPaddingPx) / containerWidthPx.toFloat())
                        .coerceIn(0f, 1f)
                )
                .clip(shape)
                .background(Color(0x14E9A12D))
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Приготовить!",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF171717)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset {
                    IntOffset(
                        x = horizontalPaddingPx + thumbOffset.roundToInt(),
                        y = 0
                    )
                }
                .size(thumbSize)
                .clip(CircleShape)
                .background(Color(0xFFE9A12D))
                .anchoredDraggable(
                    state = anchoredState,
                    orientation = Orientation.Horizontal,
                    flingBehavior = flingBehavior
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "≫",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }
    }
}

@Composable
private fun DishSheet(
    modifier: Modifier = Modifier,
    state: DishDetailedState,
    onAction: (DishDetailedAction) -> Unit,
    anchoredState: AnchoredDraggableState<DishSheetValue>,
    flingBehavior: FlingBehavior,
    expandedTop: Dp,
    expandedTopPx: Float,
    collapsedTopPxFallback: Float,
    avatarSize: Dp,
    @DrawableRes dishAvatarId: Int
) {
    val sheetOffsetPx =
        if (anchoredState.offset.isNaN()) collapsedTopPxFallback
        else anchoredState.requireOffset()

    val colors = CustomTheme.colors
    val density = LocalDensity.current

    val localSheetOffsetPx = (sheetOffsetPx - expandedTopPx).coerceAtLeast(0f)
    val localSheetOffsetInt = localSheetOffsetPx.roundToInt()
    val halfAvatarPx = with(density) { (avatarSize / 2).roundToPx() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = expandedTop)
            .clipToBounds()
            .anchoredDraggable(
                state = anchoredState,
                orientation = Orientation.Vertical,
                flingBehavior = flingBehavior
            )
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(0, localSheetOffsetInt) }
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(colors.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(avatarSize / 2))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        DishMetaInfoCard(
                            modifier = Modifier.fillMaxWidth(),
                            minutes = state.minutes,
                            difficultyLvl = state.difficultyLvl,
                            spicyLevel = state.spicyLvl,
                            allergens = state.allergensList,
                            mealType = state.mealType,
                            rating = state.rating,
                            ratingAmount = state.ratingAmount,
                            kcal = state.kcal,
                            isFlameIconRed = state.isFlameIconRed
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = state.description,
                            style = CustomTheme.typography.helvetica.titleMedium,
                            fontWeight = FontWeight.Light,
                            color = colors.tertiaryText
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Ингредиенты:",
                            style = CustomTheme.typography.helvetica.headlineSmall,
                            fontWeight = FontWeight.Normal,
                            color = colors.text
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(state.ingredientsList) { ingredient ->
                                Column(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(colors.ingredientSurface)
                                        .padding(horizontal = 28.dp, vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = ingredient.name,
                                        style = CustomTheme.typography.nunito.titleSmall,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 18.sp,
                                        color = colors.text
                                    )
                                    Text(
                                        text = "${ingredient.count} ${ingredient.unitOfMeasurement}",
                                        style = CustomTheme.typography.nunito.bodySmall,
                                        color = colors.text
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StartCookSwipeButton(
                        modifier = Modifier.weight(1f),
                        onSwiped = { onAction(DishDetailedAction.StartCook) }
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    CartBubble(
                        onClick = { onAction(DishDetailedAction.AddToCart) }
                    )
                }
            }
        }

        DishAvatar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset {
                    IntOffset(
                        x = 0,
                        y = localSheetOffsetInt - halfAvatarPx
                    )
                }
                .size(avatarSize),
            dishAvatarId = dishAvatarId
        )
    }
}

@Composable
private fun DishMetaInfoCard(
    modifier: Modifier = Modifier,
    minutes: Int,
    difficultyLvl: Int,
    spicyLevel: Int,
    allergens: List<Allergen>,
    mealType: String,
    rating: Double,
    ratingAmount: Int,
    kcal: Int,
    isFlameIconRed: Boolean
) {
    val colors = CustomTheme.colors

    Column(
        modifier = modifier.background(colors.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoIconValueBlock(
                modifier = Modifier,
                iconRes = R.drawable.timer_ic,
                text = "$minutes минут",
                iconTint = colors.text
            )
            InfoIconValueBlock(
                modifier = Modifier,
                iconRes = R.drawable.flame_ic,
                text = "$kcal kcal",
                iconTint = if (isFlameIconRed) colors.flameColor else colors.text
            )
            InfoIconValueBlock(
                modifier = Modifier,
                iconRes = R.drawable.star_ic,
                text = "$rating ($ratingAmount)",
                iconTint = colors.accentColor
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconLevelBlock(
                modifier = Modifier,
                title = "Сложность",
                currentLvl = difficultyLvl,
                iconId = R.drawable.chef_hat_ai,
                activeColor = colors.accentColor
            )
            IconLevelBlock(
                modifier = Modifier,
                title = "Острота",
                currentLvl = spicyLevel,
                iconId = R.drawable.hot_pepper_ic,
                activeColor = colors.likeColor
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        InfoTitleValueBlock(
            title = "Кухня",
            value = mealType.lowercase()
        )
        Spacer(modifier = Modifier.height(16.dp))
        InfoTitleValueBlock(
            title = "Распространенный аллерген",
            value = allergens.takeIf { it.isNotEmpty() }
                ?.joinToString(", ") { it.name }
                ?: "Не указано"
        )
    }
}

@Composable
private fun IconLevelBlock(
    modifier: Modifier = Modifier,
    title: String,
    currentLvl: Int,
    maxLevel: Int = 5,
    @DrawableRes iconId: Int,
    activeColor: Color
) {
    val colors = CustomTheme.colors

    Column(modifier = modifier) {
        Text(
            text = title,
            style = CustomTheme.typography.nunito.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = colors.text
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(maxLevel) { index ->
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = null,
                    tint = if (index < currentLvl) activeColor else colors.secondaryText,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun InfoTitleValueBlock(
    title: String,
    value: String
) {
    val colors = CustomTheme.colors

    Text(
        text = title,
        style = CustomTheme.typography.nunito.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = colors.text
    )
    Text(
        text = value,
        style = CustomTheme.typography.nunito.bodyLarge,
        color = colors.text.copy(alpha = 0.8f)
    )
}

@Composable
private fun InfoIconValueBlock(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    text: String,
    iconTint: Color
) {
    val colors = CustomTheme.colors

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = CustomTheme.typography.nunito.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = colors.text
        )
    }
}

@Preview
@Composable
private fun DishDetailedScreenPreview() {
    UfoodTheme {
        DishDetailedScreen(
            state = DishDetailedState(),
            onAction = {}
        )
    }
}
