package bob.colbaskin.cookly.home.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.common.utils.clickableWithoutRipple
import coil3.compose.AsyncImage

@Composable
fun DishCard(
    modifier: Modifier = Modifier,
    title: String,
    minutes: Int,
    dishImageUrl: String,
    @DrawableRes fallbackImageRes: Int = R.drawable.fallback_avatar,
    rating: Double,
    ratingAmount: Int,
    kcal: Int,
    spicyLevel: Int,
    difficultyLevel: Int,
    isFlameIconRed: Boolean,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(21.dp))
            .background(CustomTheme.colors.dishCardBackground)
            .clickableWithoutRipple { onClick() }
            .width(193.dp)
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            color = CustomTheme.colors.text,
            style = CustomTheme.typography.helvetica.titleLarge,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            DishDataIcon(
                modifier = Modifier.weight(1f),
                text = "$minutes min",
                containerColor = CustomTheme.colors.statsCardBackground,
                dishDataIcon = R.drawable.timer_ic
            )
            DishDataIcon(
                modifier = Modifier.weight(1f),
                text = "$difficultyLevel lvl",
                containerColor = CustomTheme.colors.statsCardBackground,
                dishDataIcon = R.drawable.chef_hat_ai
            )
        }
        Box(
            modifier = Modifier
                .size(131.dp)
                .clip(CircleShape)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = dishImageUrl,
                fallback = painterResource(id = fallbackImageRes),
                error = painterResource(id = fallbackImageRes),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            DishDataIcon(
                modifier = Modifier.weight(1f),
                text = "$rating($ratingAmount)",
                containerColor = CustomTheme.colors.statsCardBackground,
                dishDataIcon = R.drawable.star_ic
            )
            Spacer(modifier = Modifier.width(8.dp))
            DishDataIcon(
                modifier = Modifier.weight(1f),
                text = "$kcal kcal",
                containerColor = CustomTheme.colors.statsCardBackground,
                dishDataIcon = R.drawable.flame_ic,
                isFlameIconRed = isFlameIconRed
            )
        }
        if (spicyLevel > 1) {
            Row(modifier = Modifier.fillMaxWidth()) {
                DishDataIcon(
                    text = "$spicyLevel spicy",
                    containerColor = CustomTheme.colors.statsCardBackground,
                    dishDataIcon = R.drawable.hot_pepper_ic,
                    isFlameIconRed = true
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = false)
@Composable
fun DishCardPreview() {
    UfoodTheme {
        DishCard(
            title = "Fried Shrimp",
            minutes = 20,
            dishImageUrl = "https://www.image.com/photos/bybus",
            rating = 4.8,
            ratingAmount = 168,
            kcal = 150,
            isFlameIconRed = false,
            spicyLevel = 1,
            difficultyLevel = 2,
            onClick = {}
        )
    }
}
