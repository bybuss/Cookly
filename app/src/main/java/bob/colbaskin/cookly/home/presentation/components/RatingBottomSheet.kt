package bob.colbaskin.cookly.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingBottomSheet(
    rating: Int,
    isLoading: Boolean,
    onRatingClick: (Int) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = CustomTheme.colors

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.background,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(60.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(colors.tertiaryText.copy(alpha = 0.35f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .padding(horizontal = 18.dp)
                .padding(bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Вам понравилось это блюдо?",
                    style = CustomTheme.typography.inter.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colors.text,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "×",
                    style = CustomTheme.typography.inter.headlineSmall,
                    color = colors.tertiaryText,
                    modifier = Modifier.clickable(
                        enabled = !isLoading,
                        onClick = onDismiss
                    )
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(5) { index ->
                    val value = index + 1
                    Image(
                        painter = painterResource(id = R.drawable.rating_star_ic),
                        contentDescription = null,
                        modifier = Modifier
                            .size(42.dp)
                            .alpha(if (value <= rating) 1f else 0.3f)
                            .clickable(enabled = !isLoading) {
                                onRatingClick(value)
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                enabled = rating > 0 && !isLoading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF25B943),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF25B943).copy(alpha = 0.45f),
                    disabledContentColor = Color.White.copy(alpha = 0.7f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = "Отправить",
                    style = CustomTheme.typography.inter.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
