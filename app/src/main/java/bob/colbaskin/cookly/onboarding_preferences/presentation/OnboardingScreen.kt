package bob.colbaskin.cookly.onboarding_preferences.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.navigation.graphs.Graphs
import bob.colbaskin.cookly.onboarding_preferences.domain.models.IngredientGroup

@Composable
fun OnboardingScreenRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val effect = viewModel.effect.collectAsState(initial = null).value

    LaunchedEffect(effect) {
        when (effect) {
            OnboardingEffect.CompleteOnboarding -> {
                navController.navigate(Graphs.Main) {
                    popUpTo(Graphs.Onboarding) { inclusive = true }
                }
            }
            null -> Unit
        }
    }

    OnboardingScreen(
        modifier = modifier.padding(16.dp),
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun OnboardingScreen(
    modifier: Modifier = Modifier,
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.cheif_ai_avatar),
                contentDescription = null,
                modifier = Modifier
                    .size(42.dp)
                    .clip(shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Шевчик",
                    style = CustomTheme.typography.inter.titleMedium,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CustomTheme.colors.text
                )
                Text(
                    text = "Спрашивает...",
                    style = CustomTheme.typography.inter.titleMedium,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = CustomTheme.colors.secondaryText
                )
            }
        }
        Spacer(modifier = Modifier.height(76.dp))
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Есть Аллергия или предпочтения? \n" +
                            "Исключим выбранную \n" +
                            "группу ингредиентов",
                    style = CustomTheme.typography.helvetica.headlineSmall,
                    color = CustomTheme.colors.text
                )
                when (val groupsState = state.ingredientGroupsState) {
                    UiState.Idle,
                    UiState.Loading -> {
                        CircularProgressIndicator(color = CustomTheme.colors.secondAccentColor)
                    }
                    is UiState.Error -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = groupsState.title,
                                style = CustomTheme.typography.inter.bodyMedium,
                                color = CustomTheme.colors.secondaryText
                            )
                            Button(
                                onClick = { onAction(OnboardingAction.LoadIngredientGroups) },
                                enabled = !state.isSaving,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CustomTheme.colors.secondAccentColor,
                                    contentColor = CustomTheme.colors.text,
                                    disabledContainerColor = CustomTheme.colors.secondAccentColor.copy(alpha = 0.5f),
                                    disabledContentColor = CustomTheme.colors.secondaryText
                                ),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "Попробовать снова",
                                    style = CustomTheme.typography.inter.bodyMedium
                                )
                            }
                        }
                    }
                    is UiState.Success<List<IngredientGroup>> -> {
                        IngredientGroupsChoiceContent(
                            ingredientGroups = groupsState.data,
                            selectedIngredientGroupIds = state.selectedIngredientGroupIds,
                            onToggleIngredientGroup = {
                                onAction(OnboardingAction.ToggleIngredientGroup(it))
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSaving,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomTheme.colors.secondAccentColor,
                    contentColor = CustomTheme.colors.text,
                    disabledContainerColor = CustomTheme.colors.secondAccentColor.copy(alpha = 0.5f),
                    disabledContentColor = CustomTheme.colors.secondaryText
                ),
                onClick = { onAction(OnboardingAction.Finish) }
            ) {
                Text(
                    text = if (state.isSaving) "Сохраняем..." else "Дальше"
                )
            }
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSaving,
                onClick = { onAction(OnboardingAction.Skip) },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = CustomTheme.colors.secondAccentColor,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = CustomTheme.colors.secondaryText
                )
            ) {
                Text(text = "Пропустить")
            }
        }
    }
}

@Composable
private fun IngredientGroupsChoiceContent(
    ingredientGroups: List<IngredientGroup>,
    selectedIngredientGroupIds: Set<Int>,
    onToggleIngredientGroup: (Int) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ingredientGroups.forEach { group ->
            val isSelected = selectedIngredientGroupIds.contains(group.id)

            IngredientGroupChip(
                title = group.title,
                isSelected = isSelected,
                onClick = { onToggleIngredientGroup(group.id) }
            )
        }
    }
}

@Composable
private fun IngredientGroupChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = CustomTheme.colors
    val boxColor = if (isSelected) colors.selectedPreferences else colors.secondaryCardBackground

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(boxColor)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = CustomTheme.typography.inter.bodyMedium,
            color = colors.text
        )
    }
}
