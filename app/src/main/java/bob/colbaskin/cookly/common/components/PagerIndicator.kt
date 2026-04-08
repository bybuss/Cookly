package bob.colbaskin.cookly.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme

@Composable
fun PagerIndicator(
    modifier: Modifier = Modifier,
    currentPage: Int,
    pageCount: Int,
    selectedColor: Color = CustomTheme.colors.secondAccentColor,
    unselectedColor: Color = CustomTheme.colors.secondAccentColor.copy(alpha = 0.3f),
    spacedByDp: Dp = 8.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacedByDp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { pageIndex ->
            val isSelected = pageIndex == currentPage
            val color = if (isSelected) selectedColor else unselectedColor

            Box(
                modifier = Modifier
                    .weight(1f)
                    .size(8.dp)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(color)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PagerIndicatorPreview() {
    UfoodTheme {
        PagerIndicator(
            modifier = Modifier.padding(16.dp),
            currentPage = 1,
            pageCount = 2,
            selectedColor = CustomTheme.colors.secondAccentColor,
            unselectedColor = CustomTheme.colors.secondAccentColor.copy(alpha = 0.3f),
            spacedByDp = 8.dp
        )
    }
}
