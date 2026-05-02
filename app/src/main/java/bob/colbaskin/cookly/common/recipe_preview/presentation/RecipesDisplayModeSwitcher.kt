package bob.colbaskin.cookly.common.recipe_preview.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.recipe_preview.domain.models.RecipesDisplayMode

@Composable
fun RecipesDisplayModeSwitcher(
    modifier: Modifier = Modifier,
    selectedMode: RecipesDisplayMode,
    onModeClick: (RecipesDisplayMode) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Рецепты",
            color = CustomTheme.colors.text,
            style = CustomTheme.typography.madeInfinity.headlineSmall,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(CustomTheme.colors.secondaryCardBackground)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RecipesDisplayModeChip(
                text = "Баннеры",
                iconRes = R.drawable.list_ic,
                selected = selectedMode == RecipesDisplayMode.Banners,
                onClick = { onModeClick(RecipesDisplayMode.Banners) }
            )
            RecipesDisplayModeChip(
                text = "Карточки",
                iconRes = R.drawable.grid_ic,
                selected = selectedMode == RecipesDisplayMode.Cards,
                onClick = { onModeClick(RecipesDisplayMode.Cards) }
            )
        }
    }
}

@Composable
private fun RecipesDisplayModeChip(
    text: String,
    iconRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = CustomTheme.colors

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(
                if (selected) colors.secondAccentColor
                else colors.secondaryCardBackground
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint =
                if (selected) colors.background
                else colors.secondaryText
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = text,
            style = CustomTheme.typography.inter.bodyMedium,
            color =
                if (selected) colors.background
                else colors.secondaryText
        )
    }
}
