package bob.colbaskin.ufood.home.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bob.colbaskin.ufood.R
import bob.colbaskin.ufood.common.design_system.theme.CustomTheme
import bob.colbaskin.ufood.common.design_system.theme.UfoodTheme

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER
}

@Composable
fun MealsCard(modifier: Modifier = Modifier, mealType: MealType = MealType.BREAKFAST) {

    @DrawableRes val background: Int = when (mealType) {
        MealType.BREAKFAST -> R.drawable.breakfast_background
        MealType.LUNCH -> R.drawable.lunch_background
        MealType.DINNER -> R.drawable.dinner_backgound
    }

    @DrawableRes val logo: Int = when (mealType) {
        MealType.BREAKFAST -> R.drawable.breakfast_logo
        MealType.LUNCH -> R.drawable.lunch_logo
        MealType.DINNER -> R.drawable.dinner_logo
    }

    @StringRes val text: Int = when (mealType) {
        MealType.BREAKFAST -> R.string.breakfast_card_text
        MealType.LUNCH -> R.string.lunch_card_text
        MealType.DINNER -> R.string.dinner_card_text
    }

    Card(
        modifier = modifier
            .size(107.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CustomTheme.colors.primaryButton),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = painterResource(background),
                contentDescription = null,
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(66.dp),
                    painter = painterResource(logo),
                    contentDescription = null,
                )
                Text(
                    text = stringResource(text),
                    color = Color.White,
                    style = CustomTheme.typography.nunito.titleMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun MealsCardPreview() {
    UfoodTheme {
        MealsCard()
    }
}
