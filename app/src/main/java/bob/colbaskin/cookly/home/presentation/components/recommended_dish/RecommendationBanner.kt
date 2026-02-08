package bob.colbaskin.cookly.home.presentation.components.recommended_dish

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme

@Composable
fun RecommendationBanner(
    modifier: Modifier = Modifier,
    cardTitle: String,
    @DrawableRes backgroundImage: Int, // FIXME: потом будет url и через AsyncImage нужно будет
    rating: Double,
    ratingAmount: Int,
    minutes: Int,
    kcal: Int,
    isFlameIconRed: Boolean = false,
    containerColor: Color = Color.White,
    border: Boolean = true,
    isLeftCard: Boolean = false,
    backgroundHexColor: String? = null,
    isSecondFilled: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(141.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            contentColor = Color.White,
            containerColor =
                backgroundHexColor?.let { Color(it.toColorInt()) } ?: containerColor
        ),
        border = if (border) BorderStroke(1.dp, Color.Gray) else null
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
        ) {
            Image(
                painter = painterResource(backgroundImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .requiredSize(197.dp)
                    .align(if (isLeftCard) Alignment.TopStart else Alignment.TopEnd)
                    .offset(x = if (isLeftCard) (-70).dp else 70.dp)
                    .clip(CircleShape)
            )
            Text(
                text = cardTitle,
                style = CustomTheme.typography.helvetica.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(
                        start = if (!isLeftCard) 16.dp else 0.dp,
                        end = if (!isLeftCard) 0.dp else 16.dp,
                        top = 8.dp
                    )
                    .align(if (isLeftCard) Alignment.TopEnd else Alignment.TopStart),
                color =
                    if (backgroundHexColor != null && !isSecondFilled) Color.White
                    else CustomTheme.colors.text
            )

            Row(
                modifier = Modifier
                    .align(alignment =
                        if (isLeftCard) Alignment.BottomEnd
                        else Alignment.BottomStart
                    )
                    .padding(
                        start = if (isLeftCard) 0.dp else 17.dp,
                        end = if (isLeftCard) 17.dp else 0.dp,
                        bottom = 12.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                DishDataIcon(
                    text = "$rating($ratingAmount)",
                    containerColor =
                        if (backgroundHexColor != null && !isSecondFilled) {
                            CustomTheme.colors.filledStatsSurface
                        } else CustomTheme.colors.outlinedStatsSurface,
                    dishDataIcon = R.drawable.star_ic
                )
                DishDataIcon(
                    text = "$minutes min",
                    containerColor =
                        if (backgroundHexColor != null && !isSecondFilled) {
                            CustomTheme.colors.filledStatsSurface
                        } else CustomTheme.colors.outlinedStatsSurface,
                    dishDataIcon = R.drawable.timer_ic
                )
                DishDataIcon(
                    text = "$kcal kcal",
                    containerColor =
                        if (backgroundHexColor != null && !isSecondFilled) {
                            CustomTheme.colors.filledStatsSurface
                        } else CustomTheme.colors.outlinedStatsSurface,
                    dishDataIcon = R.drawable.flame_ic,
                    isFlameIconRed = isFlameIconRed
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RightOutlinedRecommendationCardPreview(modifier: Modifier = Modifier) {
    UfoodTheme {
        RecommendationBanner(
            modifier = modifier.padding(16.dp),
            cardTitle = "Fried Shrimp",
            backgroundImage = R.drawable.fried_egg_backgroiund,
            rating = 4.8,
            ratingAmount = 163,
            minutes = 20,
            kcal = 150,
            isFlameIconRed = true,
            containerColor = Color.Transparent,
            border = true,
            isLeftCard = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LeftOutlinedRecommendationCardPreview(modifier: Modifier = Modifier) {
    UfoodTheme {
        RecommendationBanner(
            modifier = modifier.padding(16.dp),
            cardTitle = "Fried Shrimp",
            backgroundImage = R.drawable.smoothie_background,
            rating = 4.8,
            ratingAmount = 163,
            minutes = 20,
            kcal = 150,
            isFlameIconRed = true,
            containerColor = Color.Transparent,
            border = true,
            isLeftCard = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilledRecommendationCardPreview(modifier: Modifier = Modifier) {
    UfoodTheme {
        RecommendationBanner(
            modifier = modifier.padding(16.dp),
            cardTitle = "Fried Shrimp",
            backgroundImage = R.drawable.shrimp_soup_image,
            rating = 4.8,
            ratingAmount = 163,
            minutes = 20,
            kcal = 150,
            isFlameIconRed = true,
            border = false,
            backgroundHexColor = "#B9480D",
            isLeftCard = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilledRecommendationCardPreview2(modifier: Modifier = Modifier) {
    UfoodTheme {
        RecommendationBanner(
            modifier = modifier.padding(16.dp),
            cardTitle = "Fried Shrimp",
            backgroundImage = R.drawable.muesli_background,
            rating = 4.8,
            ratingAmount = 163,
            minutes = 20,
            kcal = 150,
            isFlameIconRed = true,
            border = false,
            backgroundHexColor = "#F4F4F4",
            isLeftCard = false,
            isSecondFilled = true
        )
    }
}
