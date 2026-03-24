package bob.colbaskin.cookly.onboarding_preferences.presentation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.onboarding_preferences.domain.models.AllergyOption

@Composable
fun AllergyChoicePage(
    selectedAllergies: Set<AllergyOption>,
    onToggleAllergy: (AllergyOption) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Есть Аллергия? \nИсключим выбранный \nингредиент",
            style = CustomTheme.typography.helvetica.headlineSmall,
            color = CustomTheme.colors.text
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AllergyOption.entries.forEach { allergy ->
                val isSelected = selectedAllergies.contains(allergy)
                AllergyChip(
                    allergy = allergy,
                    isSelected = isSelected,
                    onClick = { onToggleAllergy(allergy) }
                )
            }
        }
    }
}

@Composable
fun AllergyChip(
    allergy: AllergyOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val boxColor = if (isSelected) CustomTheme.colors.selectedPreferences
    else CustomTheme.colors.secondaryCardBackground
    val textColor = CustomTheme.colors.text

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(boxColor)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = allergy.displayName,
            style = CustomTheme.typography.inter.bodyMedium,
            color = textColor
        )
    }
}
