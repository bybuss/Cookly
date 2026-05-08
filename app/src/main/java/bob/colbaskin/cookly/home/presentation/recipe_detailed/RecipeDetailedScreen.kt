package bob.colbaskin.cookly.home.presentation.recipe_detailed

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.components.SheetTopBar
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.home.domain.models.cook_steps.COOK_STEPS_ARGS_KEY
import bob.colbaskin.cookly.home.domain.models.cook_steps.toCookStepsNavArgs
import bob.colbaskin.cookly.home.domain.models.recipe_detailed.StartCookSwipeAnchor
import bob.colbaskin.cookly.home.domain.models.recipe_detailed.RecipeDetailed
import bob.colbaskin.cookly.home.data.models.recipe_detailed.formatQuantity
import bob.colbaskin.cookly.home.data.models.recipe_detailed.toDomainMealTime
import bob.colbaskin.cookly.home.data.models.recipe_detailed.toReviewDateText
import bob.colbaskin.cookly.home.domain.models.recipe_detailed.PubRecipeRequestStatus
import bob.colbaskin.cookly.home.presentation.components.RatingBottomSheet
import bob.colbaskin.cookly.navigation.Screens
import coil3.compose.AsyncImage
import compose.icons.TablerIcons
import compose.icons.tablericons.Edit
import compose.icons.tablericons.Trash
import kotlinx.coroutines.launch

@Composable
fun RecipeDetailedScreenRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: RecipeDetailedViewModel = hiltViewModel()
) {
    val recipeId: Int =
        navController.currentBackStackEntry?.arguments?.getInt("recipeId") ?: -1
    val isModerationReview: Boolean =
        navController.currentBackStackEntry?.arguments?.getBoolean("isModerationReview") ?: false
    val state = viewModel.state
    val scope = rememberCoroutineScope()

    LaunchedEffect(recipeId) {
        viewModel.loadRecipe(recipeId)
    }

    LaunchedEffect(state.recipeState) {
        if (state.recipeState is UiState.Error) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = state.recipeState.title,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    LaunchedEffect(state.addToCartState) {
        if (state.addToCartState is UiState.Error) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = state.addToCartState.title,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    LaunchedEffect(state.setRateState) {
        if (state.setRateState is UiState.Error) {
            snackbarHostState.showSnackbar(
                message = state.setRateState.title,
                duration = SnackbarDuration.Short
            )
        }
    }

    LaunchedEffect(state.requestPublishState) {
        if (state.requestPublishState is UiState.Error) {
            snackbarHostState.showSnackbar(
                message = state.requestPublishState.title,
                duration = SnackbarDuration.Short
            )
        }
    }

    LaunchedEffect(state.approveRecipeState) {
        when (val approveState = state.approveRecipeState) {
            is UiState.Error -> {
                snackbarHostState.showSnackbar(
                    message = approveState.title,
                    duration = SnackbarDuration.Short
                )
            }
            is UiState.Success -> navController.popBackStack()
            else -> Unit
        }
    }

    LaunchedEffect(state.rejectRecipeState) {
        when (val rejectState = state.rejectRecipeState) {
            is UiState.Error -> {
                snackbarHostState.showSnackbar(
                    message = rejectState.title,
                    duration = SnackbarDuration.Short
                )
            }
            is UiState.Success -> navController.popBackStack()
            else -> Unit
        }
    }

    LaunchedEffect(state.withdrawPublishRequestState) {
        if (state.withdrawPublishRequestState is UiState.Error) {
            snackbarHostState.showSnackbar(
                message = state.withdrawPublishRequestState.title,
                duration = SnackbarDuration.Short
            )
        }
    }

    LaunchedEffect(state.deleteRecipeState) {
        when (val deleteState = state.deleteRecipeState) {
            is UiState.Error -> {
                snackbarHostState.showSnackbar(
                    message = deleteState.title,
                    duration = SnackbarDuration.Short
                )
            }
            is UiState.Success -> navController.popBackStack()
            else -> Unit
        }
    }

    LaunchedEffect(state.startCookingState) {
        when (val startCookingState = state.startCookingState) {
            is UiState.Error -> {
                snackbarHostState.showSnackbar(
                    message = startCookingState.title,
                    duration = SnackbarDuration.Short
                )
            }

            is UiState.Success<Int> -> {
                val recipe = (state.recipeState as? UiState.Success)?.data ?: return@LaunchedEffect
                val cookingSessionId = startCookingState.data
                val args = recipe.toCookStepsNavArgs(
                    cookingSessionId = cookingSessionId
                )

                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(COOK_STEPS_ARGS_KEY, args)
                navController.navigate(Screens.CookSteps)

                viewModel.onAction(
                    RecipeDetailedAction.ChangeActiveStep(cookingSessionId = cookingSessionId)
                )
                viewModel.onAction(RecipeDetailedAction.ConsumeStartCookingResult)
            }

            else -> Unit
        }
    }

    RecipeDetailedScreen(
        modifier = modifier,
        state = state,
        isModerationReview = isModerationReview,
        onAction = { action ->
            when (action) {
                RecipeDetailedAction.NavigateBack -> navController.popBackStack()
                RecipeDetailedAction.NavigateProfile -> navController.navigate(Screens.Profile)
                RecipeDetailedAction.NavigateEditRecipe -> {
                    navController.navigate(Screens.EditRecipe(recipeId))
                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun RecipeDetailedScreen(
    modifier: Modifier = Modifier,
    state: RecipeDetailedState,
    isModerationReview: Boolean,
    onAction: (RecipeDetailedAction) -> Unit
) {
    when (state.recipeState) {
        is UiState.Success -> {
            RecipeDetailedContent(
                modifier = modifier,
                recipe = state.recipeState.data,
                state = state,
                isModerationReview = isModerationReview,
                onAction = onAction
            )
        }
        is UiState.Error -> {
            RecipeDetailedErrorScreen(
                modifier = modifier,
                title = state.recipeState.title,
                onBackClick = { onAction(RecipeDetailedAction.NavigateBack) }
            )
        }
        else -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = CustomTheme.colors.accentColor)
            }
        }
    }
    if (state.isAddToCartSheetVisible) {
        AddRecipeIngredientsToCartBottomSheet(
            portions = state.portions,
            ingredients = state.cartIngredients,
            isLoading = state.addToCartState is UiState.Loading,
            onDismiss = { onAction(RecipeDetailedAction.HideAddToCartSheet) },
            onIncreasePortions = { onAction(RecipeDetailedAction.IncreasePortions) },
            onDecreasePortions = { onAction(RecipeDetailedAction.DecreasePortions) },
            onToggleIngredient = { cartKey ->
                onAction(RecipeDetailedAction.ToggleCartIngredient(cartKey))
            },
            onConfirm = {
                onAction(RecipeDetailedAction.ConfirmAddSelectedIngredientsToCart)
            }
        )
    }
    if (state.isRejectRecipeSheetVisible) {
        RejectRecipeBottomSheet(
            feedback = state.rejectFeedback,
            isLoading = state.rejectRecipeState is UiState.Loading,
            onFeedbackChange = { feedback ->
                onAction(RecipeDetailedAction.ChangeRejectFeedback(feedback))
            },
            onDismiss = {
                onAction(RecipeDetailedAction.HideRejectRecipeSheet)
            },
            onSubmit = {
                onAction(RecipeDetailedAction.RejectRecipe)
            }
        )
    }
}

@Composable
private fun RecipeDetailedErrorScreen(
    modifier: Modifier = Modifier,
    title: String,
    text: String? = null,
    onBackClick: () -> Unit
) {
    val colors = CustomTheme.colors

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = CustomTheme.typography.inter.headlineSmall,
            color = colors.text,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        text?.let {
            Text(
                text = it,
                style = CustomTheme.typography.nunito.bodyLarge,
                color = colors.tertiaryText,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        Button(
            onClick = onBackClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.accentColor,
                contentColor = colors.invertedText
            )
        ) {
            Text("Назад")
        }
    }
}

@Composable
private fun RecipeDetailedContent(
    modifier: Modifier = Modifier,
    recipe: RecipeDetailed,
    state: RecipeDetailedState,
    isModerationReview: Boolean,
    onAction: (RecipeDetailedAction) -> Unit
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
                initialValue = RecipeDetailedSheetValue.COLLAPSED
            )
        }

        val anchors = remember(expandedTopPx, collapsedTopPx) {
            DraggableAnchors {
                RecipeDetailedSheetValue.EXPANDED at expandedTopPx
                RecipeDetailedSheetValue.COLLAPSED at collapsedTopPx
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

        RecipeBackground(
            modifier = Modifier
                .fillMaxWidth()
                .height(collapsedTop + avatarSize / 2 + 24.dp)
                .align(Alignment.TopStart),
            dishName = recipe.title,
            dishImageUrl = recipe.imageUrl,
            fallbackImageRes = state.dishAvatarFallback
        )
        RecipeSheet(
            modifier = Modifier.zIndex(3f),
            onAction = onAction,
            state = state,
            isModerationReview = isModerationReview,
            recipe = recipe,
            anchoredState = sheetState,
            flingBehavior = flingBehavior,
            expandedTop = expandedTop,
            expandedTopPx = expandedTopPx,
            collapsedTopPxFallback = collapsedTopPx,
            avatarSize = avatarSize,
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
                liquidBoxText = recipe.mealTime.toDomainMealTime(isPlural = false),
                onBackClick = { onAction(RecipeDetailedAction.NavigateBack) },
                onAvatarClick = { onAction(RecipeDetailedAction.NavigateProfile) },
                avatarUrl = state.avatarUrl,
                fallbackLetter = state.avatarLetter
            )
            Spacer(modifier = Modifier.height(16.dp))
            HeartBubble(
                modifier = Modifier,
                isLiked = state.isFavorite,
                onClick = { onAction(RecipeDetailedAction.ToggleLike) }
            )
            if (recipe.isAuthor) {
                Spacer(modifier = Modifier.height(12.dp))

                EditRecipeBubble(
                    onClick = { onAction(RecipeDetailedAction.NavigateEditRecipe) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                DeleteRecipeBubble(
                    isLoading = state.deleteRecipeState is UiState.Loading,
                    onClick = { onAction(RecipeDetailedAction.ShowDeleteRecipeDialog) }
                )
            }
        }
        if (state.isModeratorReviewSheetVisible) {
            val recipe = (state.recipeState as? UiState.Success)?.data

            recipe?.let {
                ModeratorReviewBottomSheet(
                    recipe = it,
                    onDismiss = {
                        onAction(RecipeDetailedAction.HideModeratorReviewSheet)
                    }
                )
            }
        }
        if (state.isRatingSheetVisible) {
            RatingBottomSheet(
                rating = state.selectedRating,
                isLoading = state.setRateState is UiState.Loading,
                onRatingClick = { rating -> onAction(RecipeDetailedAction.ChangeRating(rating)) },
                onSubmit = { onAction(RecipeDetailedAction.SubmitRating) },
                onDismiss = { onAction(RecipeDetailedAction.HideRatingSheet) }
            )
        }
        if (state.isDeleteRecipeDialogVisible) {
            DeleteRecipeConfirmDialog(
                isLoading = state.deleteRecipeState is UiState.Loading,
                onDismiss = {
                    onAction(RecipeDetailedAction.HideDeleteRecipeDialog)
                },
                onConfirm = {
                    onAction(RecipeDetailedAction.DeleteRecipe)
                }
            )
        }
    }
}

@Composable
private fun RecipeBackground(
    modifier: Modifier = Modifier,
    dishName: String,
    dishImageUrl: String?,
    @DrawableRes fallbackImageRes: Int
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = dishImageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            fallback = painterResource(id = fallbackImageRes),
            error = painterResource(id = fallbackImageRes),
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
            modifier = Modifier.offset(x = 2.dp),
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
private fun EditRecipeBubble(
    modifier: Modifier = Modifier,
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
            imageVector = TablerIcons.Edit,
            contentDescription = null,
            tint = colors.text
        )
    }
}

@Composable
private fun DeleteRecipeBubble(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val colors = CustomTheme.colors

    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(colors.background)
            .clickable(
                enabled = !isLoading,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = colors.likeColor,
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                imageVector = TablerIcons.Trash,
                contentDescription = null,
                tint = colors.likeColor
            )
        }
    }
}

@Composable
private fun RecipeAvatar(
    modifier: Modifier = Modifier,
    dishAvatarUrl: String?,
    @DrawableRes fallbackImageRes: Int
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
        AsyncImage(
            model = dishAvatarUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            fallback = painterResource(id = fallbackImageRes),
            error = painterResource(id = fallbackImageRes),
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
                modifier = Modifier.padding(start = 32.dp),
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
            Icon(
                painter = painterResource(R.drawable.double_right_arrows),
                contentDescription = null,
                tint = CustomTheme.colors.invertedText
            )
        }
    }
}

@Composable
private fun RecipeSheet(
    modifier: Modifier = Modifier,
    state: RecipeDetailedState,
    isModerationReview: Boolean,
    recipe: RecipeDetailed,
    onAction: (RecipeDetailedAction) -> Unit,
    anchoredState: AnchoredDraggableState<RecipeDetailedSheetValue>,
    flingBehavior: FlingBehavior,
    expandedTop: Dp,
    expandedTopPx: Float,
    collapsedTopPxFallback: Float,
    avatarSize: Dp
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
                        RecipeMetaInfoCard(
                            modifier = Modifier.fillMaxWidth(),
                            minutes = recipe.estimatedTime,
                            difficultyLvl = recipe.difficultyLevel,
                            spicyLevel = recipe.spicyLevel,
                            rating = recipe.rating,
                            ratingAmount = recipe.ratingCount,
                            kcal = recipe.caloriesBy100Grams,
                            isFlameIconRed = recipe.isFlameIconRed
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = recipe.description,
                            style = CustomTheme.typography.helvetica.titleMedium,
                            fontWeight = FontWeight.Light,
                            color = colors.tertiaryText
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        RecipePublicationBlock(
                            recipe = recipe,
                            isModerationReview = isModerationReview,
                            isRequestPublishLoading = state.requestPublishState is UiState.Loading,
                            isWithdrawPublishRequestLoading = state.withdrawPublishRequestState is UiState.Loading,
                            isApproveLoading = state.approveRecipeState is UiState.Loading,
                            isRejectLoading = state.rejectRecipeState is UiState.Loading,
                            onShowModeratorReview = {
                                onAction(RecipeDetailedAction.ShowModeratorReviewSheet)
                            },
                            onRequestPublish = {
                                onAction(RecipeDetailedAction.RequestPublishRecipe)
                            },
                            onWithdrawPublishRequest = {
                                onAction(RecipeDetailedAction.WithdrawPublishRequest)
                            },
                            onApprove = {
                                onAction(RecipeDetailedAction.ApproveRecipe)
                            },
                            onReject = {
                                onAction(RecipeDetailedAction.ShowRejectRecipeSheet)
                            }
                        )
                        RecipeRatingBlock(
                            modifier = Modifier.padding(top = 12.dp),
                            existedCookingSession = recipe.existedCookingSession,
                            userRate = state.userRate,
                            onRateClick = { onAction(RecipeDetailedAction.ShowRatingSheet) }
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
                            items(recipe.ingredients) { ingredient ->
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
                        onSwiped = { onAction(RecipeDetailedAction.StartCook) }
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    CartBubble(onClick = { onAction(RecipeDetailedAction.ShowAddToCartSheet) })
                }
            }
        }

        RecipeAvatar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset {
                    IntOffset(
                        x = 0,
                        y = localSheetOffsetInt - halfAvatarPx
                    )
                }
                .size(avatarSize),
            dishAvatarUrl = recipe.imageUrl,
            fallbackImageRes = state.dishAvatarFallback
        )
    }
}

@Composable
private fun RecipeMetaInfoCard(
    modifier: Modifier = Modifier,
    minutes: Int,
    difficultyLvl: Int,
    spicyLevel: Int,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddRecipeIngredientsToCartBottomSheet(
    portions: Int,
    ingredients: List<RecipeCartIngredientUi>,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onIncreasePortions: () -> Unit,
    onDecreasePortions: () -> Unit,
    onToggleIngredient: (String) -> Unit,
    onConfirm: () -> Unit
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxHeight(),
        containerColor = colors.secondaryCardBackground,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(60.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(colors.tertiaryText.copy(alpha = 0.45f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Порции",
                    style = typography.inter.titleLarge,
                    color = colors.text,
                    modifier = Modifier.weight(1f)
                )
                PortionButton(text = "−", onClick = onDecreasePortions)
                Box(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(width = 58.dp, height = 54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(colors.statsCardBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = portions.toString(),
                        style = typography.inter.titleLarge,
                        color = colors.text
                    )
                }
                PortionButton(text = "+", onClick = onIncreasePortions)
            }
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 520.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(
                    items = ingredients,
                    key = { it.cartKey }
                ) { item ->
                    CartIngredientSelectRow(
                        item = item,
                        onClick = { onToggleIngredient(item.cartKey) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onConfirm,
                enabled = !isLoading && ingredients.any { it.isSelected },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accentColor,
                    contentColor = colors.invertedText,
                    disabledContainerColor = colors.accentColor.copy(alpha = 0.45f),
                    disabledContentColor = colors.invertedText.copy(alpha = 0.7f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = colors.invertedText,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = "Добавить в список",
                    style = typography.inter.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PortionButton(
    text: String,
    onClick: () -> Unit
) {
    val colors = CustomTheme.colors

    Box(
        modifier = Modifier
            .size(width = 70.dp, height = 54.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(colors.statsCardBackground)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = CustomTheme.typography.inter.headlineSmall,
            color = if (text == "+") colors.secondAccentColor else colors.likeColor
        )
    }
}

@Composable
private fun CartIngredientSelectRow(
    item: RecipeCartIngredientUi,
    onClick: () -> Unit
) {
    val colors = CustomTheme.colors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isSelected,
            onCheckedChange = { onClick() },
            colors = CheckboxDefaults.colors(
                checkedColor = colors.accentColor,
                uncheckedColor = colors.tertiaryText,
                checkmarkColor = colors.invertedText
            )
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = item.title,
            style = CustomTheme.typography.inter.titleMedium,
            color = colors.text,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "${item.baseQuantity.formatQuantity()} ${item.unitMeasurement} = ${item.calculatedQuantity.formatQuantity()} ${item.unitMeasurement}",
            style = CustomTheme.typography.inter.bodyMedium,
            color = colors.text.copy(alpha = 0.85f),
            textAlign = TextAlign.End
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colors.strokeColor.copy(alpha = 0.5f))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModeratorReviewBottomSheet(
    recipe: RecipeDetailed,
    onDismiss: () -> Unit
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.background,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(60.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(colors.tertiaryText.copy(alpha = 0.35f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 620.dp)
                .verticalScroll(scrollState)
                .imePadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
        ) {
            Text(
                text = when (recipe.status) {
                    PubRecipeRequestStatus.APPROVED -> "Ваш рецепт был одобрен"
                    PubRecipeRequestStatus.REJECTED -> "Ваш рецепт был отклонён"
                    PubRecipeRequestStatus.PENDING -> "Рецепт находится на модерации"
                    null -> "Ответ модератора"
                },
                style = typography.inter.titleLarge,
                fontWeight = FontWeight.Bold,
                color = colors.text
            )

            Spacer(modifier = Modifier.height(8.dp))

            recipe.reviewedAt?.let {
                Text(
                    text = "Дата ревью: ${it.toReviewDateText()}",
                    style = typography.nunito.bodyLarge,
                    color = colors.tertiaryText
                )

                Spacer(modifier = Modifier.height(18.dp))
            }

            Text(
                text = "Фидбэк",
                style = typography.inter.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.text
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(colors.secondaryCardBackground)
                    .padding(16.dp)
            ) {
                Text(
                    text = recipe.feedback?.takeIf { it.isNotBlank() }
                        ?: "Модератор не оставил дополнительный комментарий.",
                    style = typography.nunito.bodyLarge,
                    color = colors.text
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accentColor,
                    contentColor = colors.invertedText
                )
            ) {
                Text(
                    text = "Понятно",
                    style = typography.inter.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun RecipePublicationBlock(
    modifier: Modifier = Modifier,
    recipe: RecipeDetailed,
    isModerationReview: Boolean,
    isRequestPublishLoading: Boolean,
    isWithdrawPublishRequestLoading: Boolean,
    isApproveLoading: Boolean,
    isRejectLoading: Boolean,
    onShowModeratorReview: () -> Unit,
    onRequestPublish: () -> Unit,
    onWithdrawPublishRequest: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography

    val hasModeratorFeedback = !recipe.feedback.isNullOrBlank()
    val isRejected = recipe.status == PubRecipeRequestStatus.REJECTED
    val isPending = recipe.status == PubRecipeRequestStatus.PENDING

    val canShowRequestPublishButton =
        recipe.isAuthor &&
                                recipe.pubRecipeRequestId == null &&
                recipe.status == null &&
                !isModerationReview

    val canWithdrawRequest =
        recipe.isAuthor &&
                recipe.pubRecipeRequestId != null &&
                isPending &&
                !isModerationReview

    Column(modifier = modifier.fillMaxWidth()) {
        when {
            isModerationReview -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(colors.statsCardBackground)
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = "Рецепт находится на модерации",
                        style = typography.inter.titleMedium,
                        color = colors.text
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onApprove,
                        enabled = !isApproveLoading && !isRejectLoading,
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.accentColor,
                            contentColor = colors.invertedText,
                            disabledContainerColor = colors.accentColor.copy(alpha = 0.45f),
                            disabledContentColor = colors.invertedText.copy(alpha = 0.7f)
                        )
                    ) {
                        if (isApproveLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = colors.invertedText,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Одобрить",
                                style = typography.inter.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = onReject,
                        enabled = !isApproveLoading && !isRejectLoading,
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.likeColor,
                            contentColor = colors.invertedText,
                            disabledContainerColor = colors.likeColor.copy(alpha = 0.45f),
                            disabledContentColor = colors.invertedText.copy(alpha = 0.7f)
                        )
                    ) {
                        if (isRejectLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = colors.invertedText,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Отклонить",
                                style = typography.inter.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            isPending -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(colors.statsCardBackground)
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = "Рецепт находится на модерации",
                        style = typography.inter.titleMedium,
                        color = colors.text
                    )
                }

                if (canWithdrawRequest) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = onWithdrawPublishRequest,
                            enabled = !isWithdrawPublishRequestLoading
                        ) {
                            if (isWithdrawPublishRequestLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = colors.likeColor,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }

                            Text(
                                text = "Отозвать заявку",
                                style = typography.inter.bodyMedium,
                                color = colors.likeColor
                            )
                        }
                    }
                }
            }

            isRejected -> {
                if (hasModeratorFeedback) {
                    Button(
                        onClick = onShowModeratorReview,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.statsCardBackground,
                            contentColor = colors.text
                        )
                    ) {
                        Text(
                            text = "Посмотреть ответ модератора",
                            style = typography.inter.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            canShowRequestPublishButton -> {
                Button(
                    onClick = onRequestPublish,
                    enabled = !isRequestPublishLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.accentColor,
                        contentColor = colors.invertedText,
                        disabledContainerColor = colors.accentColor.copy(alpha = 0.45f),
                        disabledContentColor = colors.invertedText.copy(alpha = 0.7f)
                    )
                ) {
                    if (isRequestPublishLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = colors.invertedText,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Text(
                        text = "Отправить на публикацию",
                        style = typography.inter.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            else -> Unit
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RejectRecipeBottomSheet(
    feedback: String,
    isLoading: Boolean,
    onFeedbackChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.background,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(60.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(colors.tertiaryText.copy(alpha = 0.35f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .imePadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
        ) {
            Text(
                text = "Причина отклонения",
                style = typography.inter.titleLarge,
                fontWeight = FontWeight.Bold,
                color = colors.text
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Напишите комментарий, который увидит автор рецепта.",
                style = typography.nunito.bodyLarge,
                color = colors.tertiaryText
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = feedback,
                onValueChange = onFeedbackChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 140.dp),
                placeholder = {
                    Text("Например: не хватает фото шагов приготовления")
                },
                enabled = !isLoading,
                minLines = 5,
                shape = RoundedCornerShape(18.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onSubmit,
                enabled = !isLoading && feedback.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.likeColor,
                    contentColor = colors.invertedText,
                    disabledContainerColor = colors.likeColor.copy(alpha = 0.45f),
                    disabledContentColor = colors.invertedText.copy(alpha = 0.7f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = colors.invertedText,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = "Отправить ответ",
                    style = typography.inter.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun RecipeRatingBlock(
    modifier: Modifier = Modifier,
    existedCookingSession: Boolean,
    userRate: Int?,
    onRateClick: () -> Unit
) {
    if (!existedCookingSession) return
    val colors = CustomTheme.colors
    if (userRate == null || userRate <= 0) {
        Button(
            onClick = onRateClick,
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.accentSecondSurface,
                contentColor = colors.text
            )
        ) {
            Text(
                text = "Оценить рецепт",
                style = CustomTheme.typography.inter.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(colors.statsCardBackground)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Ваша оценка:",
                    style = CustomTheme.typography.inter.titleMedium,
                    color = colors.text
                )
                Spacer(modifier = Modifier.width(4.dp))
                repeat(5) { index ->
                    Icon(
                        painter = painterResource(id = R.drawable.rating_star_ic),
                        contentDescription = null,
                        tint = if (index < userRate) colors.accentColor else colors.secondaryText,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Icon(
                imageVector = TablerIcons.Edit,
                contentDescription = null,
                tint = colors.text,
                modifier = Modifier.clickable(onClick = onRateClick)
            )
        }
    }
}

@Composable
private fun DeleteRecipeConfirmDialog(
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        containerColor = colors.background,
        title = {
            Text(
                text = "Удалить рецепт?",
                style = typography.inter.titleLarge,
                fontWeight = FontWeight.Bold,
                color = colors.text
            )
        },
        text = {
            Text(
                text = "Это действие нельзя будет отменить. Рецепт будет полностью удалён.",
                style = typography.nunito.bodyLarge,
                color = colors.tertiaryText
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = colors.likeColor,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = "Удалить",
                    color = colors.likeColor,
                    style = typography.inter.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text(
                    text = "Отмена",
                    color = colors.text,
                    style = typography.inter.titleMedium
                )
            }
        }
    )
}

@Preview
@Composable
private fun RecipeDetailedScreenPreview() {
    UfoodTheme {
        RecipeDetailedScreen(
            state = RecipeDetailedState(),
            isModerationReview = false,
            onAction = {}
        )
    }
}
