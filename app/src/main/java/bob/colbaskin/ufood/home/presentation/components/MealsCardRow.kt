package bob.colbaskin.ufood.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import bob.colbaskin.ufood.common.design_system.theme.UfoodTheme

@Composable
fun MealsCardRow(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        MealsCard(mealType = MealType.BREAKFAST)
        MealsCard(mealType = MealType.LUNCH)
        MealsCard(mealType = MealType.DINNER)
    }
}

@Preview
@Composable
fun MealsCardRowPreview() {
    UfoodTheme {
        MealsCardRow()
    }
}