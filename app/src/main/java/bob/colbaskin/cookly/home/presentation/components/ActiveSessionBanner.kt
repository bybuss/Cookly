package bob.colbaskin.cookly.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.home.domain.models.main.ActiveCookingSession
import coil3.compose.AsyncImage

@Composable
fun ActiveSessionBanner(
    session: ActiveCookingSession,
    onCancelClick: (Int) -> Unit,
    onOpenClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CustomTheme.colors

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(141.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = colors.background),
        border = BorderStroke(1.dp, colors.mealCardBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.TopStart)
                    .padding(top = 8.dp)
                    .padding(horizontal = 16.dp)
                    .padding(end = 197.dp - 70.dp)
                    .zIndex(1f)
            ) {
                Text(
                    text = "Готовится: \"${session.recipeTitle}\"",
                    style = CustomTheme.typography.helvetica.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "шаг: ${session.currentStepNumber} - ${session.stepTitle}",
                    style = CustomTheme.typography.helvetica.bodyMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        modifier = Modifier.padding(bottom = 8.dp),
                        onClick = { onCancelClick(session.sessionId) },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.likeColor
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text("Отмена", color = colors.invertedText)
                    }
                }
            }
            AsyncImage(
                model = session.recipeImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                fallback = painterResource(id = R.drawable.fallback_avatar),
                error = painterResource(id = R.drawable.fallback_avatar),
                modifier = Modifier
                    .requiredSize(197.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 70.dp)
                    .clip(CircleShape)
                    .zIndex(0f)
            )
            IconButton(
                onClick = { onOpenClick(session.recipeId) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(40.dp)
                    .background(colors.background, CircleShape)
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_up_right),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun ActiveSessionBannerPreview() {
    UfoodTheme {
        ActiveSessionBanner(
            session = ActiveCookingSession(
                sessionId = 1,
                recipeId = 1,
                recipeTitle = "Паста с томатным соусом и базиликом",
                currentStepNumber = 2,
                stepTitle = "Готовим соус",
                recipeImageUrl = "https://www.themealdb.com/images/media/meals/ustsqw1468250014.jpg"
            ),
            onCancelClick = {},
            onOpenClick = {}
        )
    }
}
