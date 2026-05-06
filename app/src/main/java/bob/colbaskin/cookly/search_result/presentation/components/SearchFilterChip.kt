package bob.colbaskin.cookly.search_result.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme

@Composable
fun SearchFilterChip(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = CustomTheme.colors

    Text(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) colors.accentColor else colors.secondaryCardBackground)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        text = text,
        color = if (selected) colors.background else colors.text,
        style = CustomTheme.typography.inter.bodyMedium,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
    )
}
