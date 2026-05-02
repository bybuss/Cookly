package bob.colbaskin.cookly.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.common.utils.clickableWithoutRipple
import bob.colbaskin.cookly.home.presentation.components.LiquidBox

@Composable
fun SheetTopBar(
    modifier: Modifier = Modifier,
    liquidBoxText: String,
    onBackClick: () -> Unit,
    onAvatarClick: () -> Unit,
    avatarUrl: String?,
    fallbackLetter: String,
) {
    val colors = CustomTheme.colors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onBackClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(-(1).dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colors.background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_left),
                    contentDescription = null,
                    tint = colors.text
                )
            }
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .clip(CircleShape)
                    .background(colors.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    text = "На главную",
                    style = CustomTheme.typography.inter.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = colors.text
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        LiquidBox(
            modifier = Modifier,
            text = liquidBoxText
        )
        Spacer(modifier = Modifier.weight(1f))
        ProfileAvatar(
            modifier = Modifier.clickableWithoutRipple(onClick = onAvatarClick),
            avatarUrl = avatarUrl,
            fallbackLetter = fallbackLetter,
            avatarSize = 40.dp
        )
    }
}

@Composable
fun SheetTopBar(
    modifier: Modifier = Modifier,
    liquidBoxText: String,
    onBackClick: () -> Unit,
    stepNumber: Int,
) {
    val colors = CustomTheme.colors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onBackClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(-(1).dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colors.background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_left),
                    contentDescription = null,
                    tint = colors.text
                )
            }
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .clip(CircleShape)
                    .background(colors.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    text = "На главную",
                    style = CustomTheme.typography.inter.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = colors.text
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        LiquidBox(
            modifier = Modifier,
            text = liquidBoxText
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .height(40.dp)
                .clip(CircleShape)
                .background(colors.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                text = "Шаг $stepNumber",
                style = CustomTheme.typography.inter.bodyMedium,
                fontWeight = FontWeight.Normal,
                color = colors.text
            )
        }
    }
}

@Preview
@Composable
fun SheetTopBarPreview() {
    UfoodTheme {
        SheetTopBar(
            liquidBoxText = "Завтраки",
            onBackClick = {},
            onAvatarClick = {},
            avatarUrl = null,
            fallbackLetter = "42"
        )
    }
}
