package bob.colbaskin.cookly.home.presentation.components.recommended_dish

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.common.utils.clickableWithoutRipple
import bob.colbaskin.cookly.home.presentation.components.DishDataIcon
import coil3.compose.AsyncImage

@Composable
fun RecommendationBanner(
    modifier: Modifier = Modifier,
    cardTitle: String,
    recipeImageUrl: String,
    rating: Double,
    ratingAmount: Int,
    minutes: Int,
    kcal: Int,
    isFlameIconRed: Boolean = false,
    containerColor: Color = CustomTheme.colors.background,
    border: Boolean = true,
    isLeftCard: Boolean = false,
    backgroundHexColor: String? = null,
    isSecondFilled: Boolean = false,
    onOpenClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(141.dp)
            .clickableWithoutRipple { onOpenClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                backgroundHexColor?.let { Color(it.toColorInt()) } ?: containerColor
        ),
        border = if (border) BorderStroke(1.dp, CustomTheme.colors.mealCardBorder) else null
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
        ) {
            AsyncImage(
                model = recipeImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                fallback = painterResource(id = R.drawable.fallback_avatar),
                error = painterResource(id = R.drawable.fallback_avatar),
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
                textAlign = if (isLeftCard) TextAlign.End else TextAlign.Start,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = if (!isLeftCard) 16.dp else 0.dp,
                        end = if (!isLeftCard) 0.dp else 16.dp,
                        top = 8.dp
                    )
                    .padding(
                        start = if (!isLeftCard) 0.dp else 197.dp - 70.dp,
                        end = if (isLeftCard) 0.dp else 197.dp - 70.dp,
                        top = 8.dp
                    )
                    .align(if (isLeftCard) Alignment.TopEnd else Alignment.TopStart),
                color =
                    if (backgroundHexColor != null && !isSecondFilled) Color.White
                    else CustomTheme.colors.text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
            IconButton(
                onClick = onOpenClick,
                modifier = Modifier
                    .align(if (isLeftCard) Alignment.BottomStart else Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(40.dp)
                    .background(CustomTheme.colors.background, CircleShape)
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_up_right),
                    contentDescription = null
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
            recipeImageUrl = "",
            rating = 4.8,
            ratingAmount = 163,
            minutes = 20,
            kcal = 150,
            isFlameIconRed = true,
            containerColor = Color.Transparent,
            border = true,
            isLeftCard = false,
            onOpenClick = {}
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
            recipeImageUrl = "",
            rating = 4.8,
            ratingAmount = 163,
            minutes = 20,
            kcal = 150,
            isFlameIconRed = true,
            containerColor = Color.Transparent,
            border = true,
            isLeftCard = true,
            onOpenClick = {}
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
            recipeImageUrl = "",
            rating = 4.8,
            ratingAmount = 163,
            minutes = 20,
            kcal = 150,
            isFlameIconRed = true,
            border = false,
            backgroundHexColor = "#B9480D",
            isLeftCard = false,
            onOpenClick = {}
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
            recipeImageUrl = "",
            rating = 4.8,
            ratingAmount = 163,
            minutes = 20,
            kcal = 150,
            isFlameIconRed = true,
            border = false,
            backgroundHexColor = "#F4F4F4",
            isLeftCard = false,
            isSecondFilled = true,
            onOpenClick = {}
        )
    }
}
