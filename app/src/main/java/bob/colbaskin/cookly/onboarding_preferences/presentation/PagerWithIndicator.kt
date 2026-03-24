package bob.colbaskin.cookly.onboarding_preferences.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme

@Composable
fun PagerWithIndicator(
    pageCount: Int,
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    content: @Composable (pageIndex: Int) -> Unit,
    onAction: (OnboardingAction) -> Unit
) {
    Column(modifier = modifier) {
        PagerIndicator(
            currentPage = pagerState.currentPage,
            pageCount = pageCount
        )
        Spacer(modifier = Modifier.height(42.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.cheif_ai_avatar),
                contentDescription = null,
                modifier = Modifier
                    .size(42.dp)
                    .clip(shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Шевчик",
                    style = CustomTheme.typography.inter.titleMedium,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CustomTheme.colors.text
                )
                Text(
                    text = "Спрашивает...",
                    style = CustomTheme.typography.inter.titleMedium,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = CustomTheme.colors.secondaryText
                )
            }
        }
        Spacer(modifier = Modifier.height(76.dp))
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState
        ) { pageIndex ->
            content(pageIndex)
        }
        Spacer(modifier = Modifier.height(40.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomTheme.colors.secondAccentColor,
                    contentColor = CustomTheme.colors.text,
                    disabledContainerColor = CustomTheme.colors.secondAccentColor.copy(alpha = 0.5f),
                    disabledContentColor = CustomTheme.colors.secondaryText
                ),
                onClick = {
                    if (pagerState.currentPage < pageCount - 1) {
                        onAction(OnboardingAction.NextPage)
                    } else {
                        onAction(OnboardingAction.Finish)
                    }
                }
            ) {
                Text(
                    text = when (pagerState.currentPage) {
                        0 -> "Дальше"
                        1 -> "Закончить"
                        else -> "text error"
                    }
                )
            }
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onAction(OnboardingAction.Skip) },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = CustomTheme.colors.secondAccentColor,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = CustomTheme.colors.secondaryText
                )
            ) {
                Text(
                    text = "Пропустить"
                )
            }
        }
    }
}

@Composable
private fun PagerIndicator(
    currentPage: Int,
    pageCount: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { pageIndex ->
            val isSelected = pageIndex == currentPage
            val color = if (isSelected) {
                CustomTheme.colors.secondAccentColor
            } else {
                CustomTheme.colors.secondAccentColor.copy(alpha = 0.3f)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .size(8.dp)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(color)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PagerIndicatorPreview() {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { 2 }
    )

    UfoodTheme {
        PagerWithIndicator(
            pageCount = 2,
            modifier = Modifier.padding(16.dp),
            pagerState = pagerState,
            content = {},
            onAction = {}
        )
    }
}
