package bob.colbaskin.cookly.home.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.home.domain.models.MealType

@Composable
fun MealsCard(
    modifier: Modifier = Modifier,
    mealType: MealType = MealType.BREAKFAST
) {
    @DrawableRes val logo: Int = when (mealType) {
        MealType.BREAKFAST -> R.drawable.breakfast_logo
        MealType.LUNCH -> R.drawable.lunch_logo
        MealType.AFTERNOON_SNACK -> TODO()
        MealType.DINNER -> R.drawable.dinner_logo
        MealType.SNACK -> TODO()
    }

    @StringRes val text: Int = when (mealType) {
        MealType.BREAKFAST -> R.string.breakfast_card_text
        MealType.LUNCH -> R.string.lunch_card_text
        MealType.AFTERNOON_SNACK -> R.string.afternoon_snack_card_text
        MealType.DINNER -> R.string.dinner_card_text
        MealType.SNACK -> R.string.snack_card_text
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(21.dp))
            .defaultMinSize(minWidth = 116.dp)
            .height(145.dp)
            .border(
                border = BorderStroke(1.dp, CustomTheme.colors.mealCardBorder),
                shape = RoundedCornerShape(21.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
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
                color = CustomTheme.colors.text,
                style = CustomTheme.typography.nunito.titleMedium,
                modifier = Modifier.offset(y = (-6).dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MealsCardPreview() {
    UfoodTheme {
        MealsCard(modifier = Modifier.padding(16.dp))
    }
}
