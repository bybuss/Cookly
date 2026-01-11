package bob.colbaskin.ufood.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bob.colbaskin.ufood.common.design_system.theme.UfoodTheme

@Composable
fun MealsCardRow(modifier: Modifier = Modifier, mealsList: List<MealType>) {
    LazyRow (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(25.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        mealsList.forEach { mealType ->
            item {
                MealsCard(mealType = mealType)
            }
        }
    }
}

@Preview
@Composable
private fun MealsCardRowPreview() {
    val mealsList = listOf(
        MealType.BREAKFAST,
        MealType.LUNCH,
        MealType.DINNER,
        MealType.DINNER,
    )

    UfoodTheme {
        MealsCardRow(mealsList = mealsList)
    }
}
