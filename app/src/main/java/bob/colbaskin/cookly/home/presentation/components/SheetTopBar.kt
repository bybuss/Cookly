package bob.colbaskin.cookly.home.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme

@Composable
fun SheetTopBar(
    modifier: Modifier = Modifier,
    liquidBoxText: String,
    onBackClick: () -> Unit,
    @DrawableRes avatarId: Int // TODO: заменить потом на ссыллку
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
        Image(
            painter = painterResource(avatarId),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
    }
}

@Preview
@Composable
fun SheetTopBarPreview() {
    UfoodTheme {
        SheetTopBar(
            liquidBoxText = "Завтраки",
            onBackClick = {},
            avatarId = R.drawable.user_avatar_mock
        )
    }
}
