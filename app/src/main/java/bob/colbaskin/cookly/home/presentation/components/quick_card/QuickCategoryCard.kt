package bob.colbaskin.cookly.home.presentation.components.quick_card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.home.domain.models.QuickCategoryType

@Composable
fun QuickCategoryCard(
    modifier: Modifier = Modifier,
    quickCategoryType: QuickCategoryType = QuickCategoryType.QUICK_COOK
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .height(37.dp)
            .background(CustomTheme.colors.quickCategoryCardBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = stringResource(quickCategoryType.textId),
            color = CustomTheme.colors.quickCategoryCardText,
            style = CustomTheme.typography.inter.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuickCategoryCardPreview() {
    UfoodTheme {
        QuickCategoryCard()
    }
}
