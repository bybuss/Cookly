package bob.colbaskin.cookly.home.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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

@Composable
fun DishCard(
    modifier: Modifier = Modifier,
    title: String,
    minutes: Int,
    @DrawableRes dishImage: Int, // FIXME: потом будет url и через AsyncImage нужно будет
    rating: Double,
    ratingAmount: Int,
    kcal: Int,
    isFlameIconRed: Boolean = false
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(21.dp))
            .background(CustomTheme.colors.dishCardBackground)
            .width(193.dp)
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
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
            modifier = Modifier
                .fillMaxWidth()
        ) {
            DishDataIcon(
                text = "$minutes min",
                containerColor = CustomTheme.colors.statsCardBackground,
                dishDataIcon = R.drawable.timer_ic
            )
        }
        Image(
            modifier = Modifier
                .size(131.dp)
                .clip(CircleShape),
            painter = painterResource(dishImage),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DishDataIcon(
                text = "$rating($ratingAmount)",
                containerColor = CustomTheme.colors.statsCardBackground,
                dishDataIcon = R.drawable.star_ic
            )
            DishDataIcon(
                text = "$kcal kcal",
                containerColor = CustomTheme.colors.statsCardBackground,
                dishDataIcon = R.drawable.flame_ic,
                isFlameIconRed = isFlameIconRed
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun DishCardPreview() {
    UfoodTheme {
        DishCard(
            title = "Fried Shrimp",
            minutes = 20,
            dishImage = R.drawable.fried_egg_backgroiund,
            rating = 4.8,
            ratingAmount = 168,
            kcal = 150,
            isFlameIconRed = false,
        )
    }
}
