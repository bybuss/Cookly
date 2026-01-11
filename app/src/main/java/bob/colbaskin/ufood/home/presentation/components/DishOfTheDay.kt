package bob.colbaskin.ufood.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bob.colbaskin.ufood.R
import bob.colbaskin.ufood.common.design_system.theme.CustomTheme
import bob.colbaskin.ufood.common.design_system.theme.UfoodTheme

@Composable
fun DishOfTheDay(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.dish_of_the_day),
            color = CustomTheme.colors.text,
            style = CustomTheme.typography.madeInfinity.headlineSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(109.dp),
            shape = RoundedCornerShape(21.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            colors = CardDefaults.cardColors(
                contentColor = Color.White,
                containerColor = CustomTheme.colors.dishOfDayCardBackground
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(21.dp))
            ) {
                Image(
                    painter = painterResource(R.drawable.shrimp_soup_image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .requiredSize(197.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = 35.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = "Fried Shrimp",
                    style = CustomTheme.typography.brightoWander.titleLarge,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 29.dp, top = 36.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DishOfTheDayPreview() {
    UfoodTheme {
        DishOfTheDay()
    }
}