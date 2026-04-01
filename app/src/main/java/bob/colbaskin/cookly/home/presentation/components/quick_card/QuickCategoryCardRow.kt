package bob.colbaskin.cookly.home.presentation.components.quick_card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.home.domain.models.QuickCategoryType
import kotlin.collections.forEach

@Composable
fun QuickCategoryCardRow(
    modifier: Modifier = Modifier,
        quickCardsList: List<QuickCategoryType>
) {
    LazyRow (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        quickCardsList.forEach { quickCategoryType ->
            item {
                QuickCategoryCard(quickCategoryType = quickCategoryType)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuickCategoryCardRowPreview() {
    val quickCardsList = listOf(
        QuickCategoryType.QUICK_COOK,
        QuickCategoryType.DIETARY,
        QuickCategoryType.HIGH_CALORIE,
        QuickCategoryType.ON_TREND,
    )

    UfoodTheme {
        QuickCategoryCardRow(quickCardsList = quickCardsList)
    }
}
