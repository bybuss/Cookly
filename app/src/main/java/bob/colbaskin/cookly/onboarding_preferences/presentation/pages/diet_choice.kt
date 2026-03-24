package bob.colbaskin.cookly.onboarding_preferences.presentation.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.onboarding_preferences.domain.models.DietOption

@Composable
fun DietChoicePage(
    selectedDiets: Set<DietOption>,
    onToggleDiet: (DietOption) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Придерживаетесь ли \nВы диеты из списка?",
                style = CustomTheme.typography.helvetica.headlineSmall,
                color = CustomTheme.colors.text
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Мы покажем Вам рецепты основанные \nна ваших предпочтениях",
                style = CustomTheme.typography.helvetica.bodyLarge,
                color = CustomTheme.colors.text
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            DietOption.entries.forEach { diet ->
                val isSelected = selectedDiets.contains(diet)
                item {
                    DietCard(
                        modifier = Modifier.weight(1f),
                        diet = diet,
                        isSelected = isSelected,
                        onClick = { onToggleDiet(diet) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DietCard(
    modifier: Modifier = Modifier,
    diet: DietOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor =
        if (isSelected) CustomTheme.colors.selectedPreferences
        else CustomTheme.colors.background

    Box(
        modifier = modifier
            .height(150.dp)
            .width(127.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = CustomTheme.colors.mealCardBorder,
                shape = RoundedCornerShape(10.dp)
            )
            .background(bgColor)
            .clickable { onClick() },

    ) {
        Image(
            painter = painterResource(id = diet.imageRes),
            contentDescription = diet.displayName,
            modifier = Modifier
                .size(126.dp)
                .padding(bottom = 18.dp)
                .clip(RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )),
            contentScale = ContentScale.FillWidth
        )
        Text(
            text = diet.displayName,
            style = CustomTheme.typography.nunito.bodyMedium,
            color = CustomTheme.colors.text,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 14.dp)
        )
    }
}

@Preview
@Composable
private fun DietCardPreview() {
    UfoodTheme {
        DietCard(
            diet = DietOption.Vegetarian,
            isSelected = false,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DietChoicePagePreview() {
    UfoodTheme {
        DietChoicePage(
            selectedDiets = emptySet(),
            onToggleDiet = {}
        )
    }
}


