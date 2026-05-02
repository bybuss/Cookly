package bob.colbaskin.cookly.home.presentation.components.meals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.common.recipe_preview.domain.models.MealTimeType

@Composable
fun MealsCardRow(
    modifier: Modifier = Modifier,
    mealsList: List<MealTimeType>,
    onClick: (String) -> Unit
) {
    LazyRow (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        mealsList.forEach { mealType ->
            item {
                MealsCard(mealType = mealType, onClick = onClick)
            }
        }
    }
}

@Preview
@Composable
private fun MealsCardRowPreview() {
    val mealsList = listOf(
        MealTimeType.BREAKFAST,
        MealTimeType.LUNCH,
        MealTimeType.SUPPER
    )

    UfoodTheme {
        MealsCardRow(mealsList = mealsList, onClick = {})
    }
}
