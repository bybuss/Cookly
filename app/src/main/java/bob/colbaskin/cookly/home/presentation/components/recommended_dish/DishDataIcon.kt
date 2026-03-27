package bob.colbaskin.cookly.home.presentation.components.recommended_dish

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme

@Composable
fun DishDataIcon(
    modifier: Modifier = Modifier,
    text: String,
    containerColor: Color,
    isFlameIconRed: Boolean = false,
    @DrawableRes dishDataIcon: Int
) {
    val iConColor: Color =
        if (isFlameIconRed) CustomTheme.colors.flameColor
        else Color.Black

    Row(
        modifier = modifier
            .height(27.dp)
            .clip(CircleShape)
            .background(color = containerColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Image(
                painter = painterResource(dishDataIcon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(iConColor),
                modifier = Modifier.size(16.dp),

                )
            Text(
                text = text,
                style = CustomTheme.typography.gilroy.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }
}
