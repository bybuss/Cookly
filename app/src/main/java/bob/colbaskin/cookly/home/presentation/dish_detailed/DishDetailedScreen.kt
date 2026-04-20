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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import kotlin.math.roundToInt
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.home.presentation.components.DishDataIcon
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
private fun StartCookSliderButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(78.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = 0.10f),
                shape = RoundedCornerShape(999.dp)
            )
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(62.dp)
                .clip(CircleShape)
                .background(Color(0xFFE9A12D)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "≫",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Приготовить!",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF171717)
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
                .zIndex(1f)
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
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Очень вкусная свежая иичница их мятых иичек с вилочкой и хлебушком сочным и перчиком ммм",
                            style = CustomTheme.typography.helvetica.titleMedium,
                            fontWeight = FontWeight.Light,
                            color = colors.tertiaryText
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Ингридиенты:",
                            style = CustomTheme.typography.helvetica.headlineSmall,
                            fontWeight = FontWeight.Normal,
                            color = colors.text
                        )
                        Spacer(modifier = Modifier.height(12.dp))
//                        FlowRow(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(8.dp),
//                            horizontalArrangement = Arrangement.spacedBy(8.dp),
//                            verticalArrangement = Arrangement.spacedBy(8.dp)
//                        ) {
//                            AllergyOption.entries.forEach { allergy ->
//                                val isSelected = selectedAllergies.contains(allergy)
//                                AllergyChip(
//                                    allergy = allergy,
//                                    isSelected = isSelected,
//                                    onClick = { onToggleAllergy(allergy) }
//                                )
//                            }
//                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StartCookSliderButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onAction(DishDetailedAction.StartCook) }
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
                .size(avatarSize)
                .zIndex(2f),
            dishAvatarId = dishAvatarId
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(0, localSheetOffsetInt) }
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp)
                .zIndex(3f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DishDataIcon(
                text = "${state.rating}(${state.ratingAmount})",
                containerColor = colors.outlinedStatsSurface,
                dishDataIcon = R.drawable.star_ic
            )
            DishDataIcon(
                modifier = Modifier.offset(y = avatarSize / 5),
                text = "${state.kcal} kcal",
                containerColor = colors.outlinedStatsSurface,
                dishDataIcon = R.drawable.flame_ic,
                isFlameIconRed = false
            )
            DishDataIcon(
                text = "${state.minutes} min",
                containerColor = colors.outlinedStatsSurface,
                dishDataIcon = R.drawable.timer_ic
            )
        }
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
