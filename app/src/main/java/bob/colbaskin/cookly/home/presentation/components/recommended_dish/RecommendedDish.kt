package bob.colbaskin.cookly.home.presentation.components.recommended_dish

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme

@Composable
fun RecommendedDish(
    modifier: Modifier = Modifier,
    title: String,
    @DrawableRes aiAvatar: Int? = null,
    recommendationCard: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
        ) {
            Text(
                text = title,
                color = CustomTheme.colors.text,
                style = CustomTheme.typography.madeInfinity.headlineSmall,
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(R.drawable.arrow_down_right),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .padding(bottom = 4.dp)
            )
            if (aiAvatar != null) {
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(aiAvatar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape),
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        recommendationCard()
    }
}

@Preview(showBackground = true)
@Composable
private fun FilledRecommendedDishPreview() {
    UfoodTheme {
        RecommendedDish(
            title = "Блюдо дня",
            aiAvatar = R.drawable.cheif_ai_avatar,
            recommendationCard = {
                RecommendationBanner(
                    modifier = Modifier,
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
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilledRecommendedDishPreview2() {
    UfoodTheme {
        RecommendedDish(
            title = "Блюдо дня",
            aiAvatar = R.drawable.cheif_ai_avatar,
            recommendationCard = {
                RecommendationBanner(
                    modifier = Modifier,
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
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RightOutlinedRecommendedDishPreview() {
    UfoodTheme {
        RecommendedDish(
            title = "Завтрак от Шефа",
            aiAvatar = R.drawable.cheif_ai_avatar,
            recommendationCard = {
                RecommendationBanner(
                    modifier = Modifier,
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
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LeftOutlinedRecommendedDishPreview() {
    UfoodTheme {
        RecommendedDish(
            title = "Завтрак от Шефа",
            aiAvatar = R.drawable.cheif_ai_avatar,
            recommendationCard = {
                RecommendationBanner(
                    modifier = Modifier,
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
        )
    }
}
