package bob.colbaskin.cookly.common.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme

@Composable
fun ClickableIconLevelBlock(
    modifier: Modifier = Modifier,
    title: String,
    currentLevel: Int,
    minLevel: Int,
    maxLevel: Int = 5,
    @DrawableRes iconId: Int,
    activeColor: Color,
    onLevelChange: (Int) -> Unit
) {
    val colors = CustomTheme.colors

    Column(modifier = modifier) {
        Text(
            text = title,
            style = CustomTheme.typography.nunito.titleLarge,
            color = colors.text
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(maxLevel) { index ->
                val level = index + 1
                val isActive = index < currentLevel
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = "$title $level",
                    tint = if (isActive) activeColor else colors.secondaryText,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            val newLevel = when {
                                minLevel == 0 && currentLevel == level -> 0
                                else -> level.coerceIn(minLevel, maxLevel)
                            }
                            onLevelChange(newLevel)
                        }
                )
            }
        }
    }
}
