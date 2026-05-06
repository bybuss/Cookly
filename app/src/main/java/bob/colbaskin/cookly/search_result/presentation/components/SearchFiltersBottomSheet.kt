package bob.colbaskin.cookly.search_result.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.components.ClickableIconLevelBlock
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.recipe_preview.domain.models.MealTimeType
import bob.colbaskin.cookly.onboarding_preferences.domain.models.IngredientGroup
import bob.colbaskin.cookly.search_result.domain.models.RecipeSearchFilters
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFiltersBottomSheet(
    modifier: Modifier = Modifier,
    filters: RecipeSearchFilters,
    ingredientGroupsState: UiState<List<IngredientGroup>>,
    onFiltersChange: (RecipeSearchFilters) -> Unit,
    onApplyClick: () -> Unit,
    onResetClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        containerColor = colors.background,
        contentColor = colors.text,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 24.dp),
                    text = "Фильтры",
                    color = colors.text,
                    style = typography.inter.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                FilterBlockTitle(text = "Группы ингредиентов")
                when (ingredientGroupsState) {
                    UiState.Idle, UiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = colors.accentColor)
                        }
                    }
                    is UiState.Error -> {
                        Text(
                            modifier = Modifier.padding(vertical = 12.dp),
                            text = ingredientGroupsState.title,
                            color = colors.secondaryText,
                            style = typography.inter.bodyMedium
                        )
                    }
                    is UiState.Success -> {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ingredientGroupsState.data.forEach { group ->
                                val selected = group.id in filters.includeIngredientGroupIds

                                SearchFilterChip(
                                    text = group.title,
                                    selected = selected,
                                    onClick = {
                                        val updatedIds =
                                            if (selected) {
                                                filters.includeIngredientGroupIds - group.id
                                            } else {
                                                filters.includeIngredientGroupIds + group.id
                                            }
                                        onFiltersChange(
                                            filters.copy(
                                                includeIngredientGroupIds = updatedIds
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                FilterSpacer()

                FilterBlockTitle(text = "Прием пищи")
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MealTimeType.entries.forEach { mealTimeType ->
                        MealTimeChip(
                            mealTimeType = mealTimeType,
                            selectedMealTimeType = filters.mealTimeType,
                            filters = filters,
                            onFiltersChange = onFiltersChange
                        )
                    }
                }

                FilterSpacer()

                FilterBlockTitle(text = "Время приготовления")
                CookingTimeSlider(filters = filters, onFiltersChange = onFiltersChange)

                FilterSpacer()

                FilterBlockTitle(text = "Калорийность на 100г.")
                CaloriesSlider(
                    filters = filters,
                    onFiltersChange = onFiltersChange
                )

                FilterSpacer()

                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ClickableIconLevelBlock(
                        title = "Сложность",
                        currentLevel = filters.maxDifficulty ?: 0,
                        minLevel = 0,
                        maxLevel = 5,
                        iconId = R.drawable.chef_hat_ai,
                        activeColor = colors.accentColor,
                        onLevelChange = { level ->
                            onFiltersChange(
                                filters.copy(
                                    maxDifficulty = if (level == 0) null else level
                                )
                            )
                        }
                    )
                    ClickableIconLevelBlock(
                        title = "Острота",
                        currentLevel = filters.maxSpicy ?: 0,
                        minLevel = 0,
                        maxLevel = 5,
                        iconId = R.drawable.hot_pepper_ic,
                        activeColor = colors.likeColor,
                        onLevelChange = { level ->
                            onFiltersChange(
                                filters.copy(
                                    maxSpicy = if (level == 0) null else level
                                )
                            )
                        }
                    )
                }

                FilterSpacer()

                FilterBlockTitle(text = "Рейтинг")
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    RatingChip(
                        text = "От 3.0",
                        value = 3.0,
                        filters = filters,
                        onFiltersChange = onFiltersChange
                    )
                    RatingChip(
                        text = "От 4.0",
                        value = 4.0,
                        filters = filters,
                        onFiltersChange = onFiltersChange
                    )
                    RatingChip(
                        text = "От 4.5",
                        value = 4.5,
                        filters = filters,
                        onFiltersChange = onFiltersChange
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.background)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onResetClick,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.accentSecondSurface,
                        contentColor = colors.text
                    )
                ) {
                    Text(text = "Сбросить")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onApplyClick,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.accentColor,
                        contentColor = colors.background
                    )
                ) {
                    Text(
                        text = "Применить",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterBlockTitle(text: String) {
    Text(
        modifier = Modifier.padding(bottom = 12.dp),
        text = text,
        color = CustomTheme.colors.text,
        style = CustomTheme.typography.inter.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun FilterSpacer() { Spacer(modifier = Modifier.height(24.dp)) }

@Composable
private fun MealTimeChip(
    mealTimeType: MealTimeType,
    selectedMealTimeType: MealTimeType?,
    filters: RecipeSearchFilters,
    onFiltersChange: (RecipeSearchFilters) -> Unit
) {
    SearchFilterChip(
        text = mealTimeType.title,
        selected = selectedMealTimeType == mealTimeType,
        onClick = {
            onFiltersChange(
                filters.copy(
                    mealTimeType = if (selectedMealTimeType == mealTimeType) null else mealTimeType
                )
            )
        }
    )
}

@Composable
private fun CaloriesSlider(
    filters: RecipeSearchFilters,
    onFiltersChange: (RecipeSearchFilters) -> Unit
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography

    val minCalories = 100
    val maxCalories = 1000
    val step = 50

    val caloriesValue = filters.maxCaloriesBy100Grams?.toInt() ?: maxCalories

    Slider(
        value = caloriesValue.toFloat(),
        onValueChange = { value ->
            val roundedValue = ((value / step).roundToInt() * step)
                .coerceIn(minCalories, maxCalories)

            onFiltersChange(
                filters.copy(
                    maxCaloriesBy100Grams =
                        if (roundedValue >= maxCalories) null else roundedValue.toDouble()
                )
            )
        },
        valueRange = minCalories.toFloat()..maxCalories.toFloat(),
        steps = ((maxCalories - minCalories) / step) - 1,
        colors = SliderDefaults.colors(
            thumbColor = colors.accentColor,
            activeTrackColor = colors.accentSecondSurface,
            inactiveTrackColor = colors.secondaryCardBackground,
            activeTickColor = colors.text,
            inactiveTickColor = colors.mealCardBorder
        )
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text =
                if (filters.maxCaloriesBy100Grams == null) {
                    "Не ограничивать"
                } else {
                    "до ${filters.maxCaloriesBy100Grams.toInt()} ккал"
                },
            color = colors.secondaryText,
            style = typography.inter.bodyMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        SearchFilterChip(
            text = "Сбросить калории",
            selected = false,
            onClick = {
                onFiltersChange(filters.copy(maxCaloriesBy100Grams = null))
            }
        )
    }
}

@Composable
private fun CookingTimeSlider(
    filters: RecipeSearchFilters,
    onFiltersChange: (RecipeSearchFilters) -> Unit
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography

    val minTime = 10
    val maxTime = 120

    val timeValue = filters.maxEstimatedCookingTime ?: maxTime

    Slider(
        value = timeValue.toFloat(),
        onValueChange = { value ->
            val roundedValue = value.roundToInt()

            onFiltersChange(
                filters.copy(
                    maxEstimatedCookingTime = if (roundedValue >= maxTime) null else roundedValue
                )
            )
        },
        valueRange = minTime.toFloat()..maxTime.toFloat(),
        steps = 10,
        colors = SliderDefaults.colors(
            thumbColor = colors.accentColor,
            activeTrackColor = colors.accentSecondSurface,
            inactiveTrackColor = colors.secondaryCardBackground,
            activeTickColor = colors.text,
            inactiveTickColor = colors.mealCardBorder
        )
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text =
                if (filters.maxEstimatedCookingTime == null) "Не ограничивать"
                else "до ${filters.maxEstimatedCookingTime} мин",
            color = colors.secondaryText,
            style = typography.inter.bodyMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        SearchFilterChip(
            text = "Сбросить время",
            selected = false,
            onClick = { onFiltersChange(filters.copy(maxEstimatedCookingTime = null)) }
        )
    }
}

@Composable
private fun RatingChip(
    text: String,
    value: Double,
    filters: RecipeSearchFilters,
    onFiltersChange: (RecipeSearchFilters) -> Unit
) {
    SearchFilterChip(
        text = text,
        selected = filters.minAvgRating == value,
        onClick = {
            onFiltersChange(
                filters.copy(
                    minAvgRating = if (filters.minAvgRating == value) null else value
                )
            )
        }
    )
}
