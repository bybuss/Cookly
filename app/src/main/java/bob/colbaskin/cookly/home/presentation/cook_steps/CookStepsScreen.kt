package bob.colbaskin.cookly.home.presentation.cook_steps

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.common.components.SheetTopBar
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.home.domain.models.cook_steps.COOK_STEPS_ARGS_KEY
import bob.colbaskin.cookly.home.domain.models.cook_steps.CookStepsNavArgs
import bob.colbaskin.cookly.home.data.models.recipe_detailed.toDomainMealTime
import bob.colbaskin.cookly.home.presentation.components.RatingBottomSheet
import bob.colbaskin.cookly.navigation.Screens
import coil3.compose.AsyncImage
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CookStepsScreenRoot(
    navController: NavHostController,
    viewModel: CookStepsViewModel = hiltViewModel()
) {
    val args = navController
        .previousBackStackEntry
        ?.savedStateHandle
        ?.get<CookStepsNavArgs>(COOK_STEPS_ARGS_KEY)

    LaunchedEffect(args) {
        args?.let {
            viewModel.onAction(CookStepsAction.Init(it))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                CookStepsEvent.NavigateBack -> navController.popBackStack()
                CookStepsEvent.NavigateHome -> navController.navigate(Screens.Home) {
                    popUpTo<Screens.CookSteps> { inclusive = true }
                }
            }
        }
    }

    CookStepsScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun CookStepsScreen(
    state: CookStepsState,
    onAction: (CookStepsAction) -> Unit
) {
    val colors = CustomTheme.colors
    val step = state.currentStep

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.54f)
                        .background(colors.text)
                ) {
                    AsyncImage(
                        model = step?.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        fallback = painterResource(id = state.fallbackImageRes),
                        error = painterResource(id = state.fallbackImageRes),
                        modifier = Modifier
                            .matchParentSize()
                            .blur(18.dp)
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(colors.text.copy(alpha = 0.62f))
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SheetTopBar(
                            modifier = Modifier,
                            liquidBoxText = state.mealType.toDomainMealTime(isPlural = false),
                            onBackClick = { onAction(CookStepsAction.BackToHome) },
                            stepNumber = state.currentStepNumber,
                        )
                        Spacer(modifier = Modifier.height(56.dp))
                        StepRecipeAvatar(
                            imageUrl = step?.imageUrl,
                            fallbackImageRes = state.fallbackImageRes
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.recipeTitle,
                            style = CustomTheme.typography.inter.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.invertedText,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.46f)
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(colors.background)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Text(
                    text = step?.title?.takeIf { it.isNotBlank() } ?: "Как готовить?",
                    style = CustomTheme.typography.inter.headlineSmall,
                    color = colors.text
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = step?.description.orEmpty(),
                    style = CustomTheme.typography.inter.bodyLarge,
                    color = colors.tertiaryText
                )
                Spacer(modifier = Modifier.weight(1f))
                StepBottomButtons(
                    isFirstStep = state.isFirstStep,
                    isLastStep = state.isLastStep,
                    onPreviousClick = { onAction(CookStepsAction.PreviousStep) },
                    onNextClick = {
                        if (state.isLastStep) {
                            onAction(CookStepsAction.FinishCooking)
                        } else {
                            onAction(CookStepsAction.NextStep)
                        }
                    }
                )
            }
        }
        if (state.isRatingSheetVisible) {
            RatingBottomSheet(
                rating = state.rating,
                isLoading = false, // FIXME: заменить на апи результ отправки рейтинга
                onRatingClick = { onAction(CookStepsAction.SetRating(it)) },
                onSubmit = { onAction(CookStepsAction.SubmitRating) },
                onDismiss = { onAction(CookStepsAction.DismissRating) }
            )
        }
    }
}

@Composable
private fun StepRecipeAvatar(
    imageUrl: String?,
    fallbackImageRes: Int
) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(CustomTheme.colors.accentColor),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            fallback = painterResource(id = fallbackImageRes),
            error = painterResource(id = fallbackImageRes),
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
        )
    }
}

@Composable
private fun StepBottomButtons(
    isFirstStep: Boolean,
    isLastStep: Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (!isFirstStep && !isLastStep) {
            OutlinedButton(
                onClick = onPreviousClick,
                modifier = Modifier
                    .weight(0.8f)
                    .height(64.dp),
                shape = RoundedCornerShape(42.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = CustomTheme.colors.text,
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = CustomTheme.colors.accentColor
                )
            ) {
                Text("Назад")
            }
        }
        Button(
            onClick = onNextClick,
            modifier = Modifier
                .weight(1.4f)
                .height(64.dp),
            shape = RoundedCornerShape(42.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomTheme.colors.accentColor,
                contentColor = CustomTheme.colors.invertedText
            )
        ) {
            Text(if (isLastStep) "Завершить" else "Дальше")
        }
    }
}

@Preview
@Composable
fun CookStepsScreenPreview() {
    UfoodTheme {
        CookStepsScreen(
            state = CookStepsState(),
            onAction = {}
        )
    }
}
