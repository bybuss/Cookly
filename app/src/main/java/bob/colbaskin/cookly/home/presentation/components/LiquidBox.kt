package bob.colbaskin.cookly.home.presentation.components

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.liquid
import io.github.fletchmckee.liquid.rememberLiquidState
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme

@Composable
fun LiquidBox(
    modifier: Modifier = Modifier,
    text: String
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val liquidState = rememberLiquidState()

        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .matchParentSize()
                    .liquefiable(liquidState)
                    .background(Color(0xFFA1968A).copy(alpha = 0.65f))
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .liquid(liquidState) {
                        frost = 25.dp
                        refraction = 0.22f
                        curve = 0.18f
                        edge = 0.12f
                        tint = Color.White.copy(alpha = 0.10f)
                        saturation = 1.05f
                        contrast = 1.02f
                    }
                    .clip(CircleShape)
            )
            Text(
                text = text,
                color = Color.White,
                style = CustomTheme.typography.inter.bodyMedium,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
            )
        }
    } else {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.85f))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.9f),
                    shape = CircleShape
                )
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                style = CustomTheme.typography.inter.bodyMedium,
                modifier = Modifier
            )
        }
    }
}

@Suppress("PreviewApiLevelMustBeValid")
@Preview(name = "API 33", apiLevel = 33, showBackground = false)
@Composable
fun BreakfastLiquidBoxPreviewApi33() {
    UfoodTheme {
        Box (
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.fried_egg_backgroiund),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.size(150.dp)
            )
            LiquidBox(text = "Завтраки")
        }
    }
}

@Suppress("PreviewApiLevelMustBeValid")
@Preview(name = "API 29", apiLevel = 29, showBackground = false)
@Composable
fun BreakfastLiquidBoxPreviewApi29() {
    UfoodTheme {
        Box (
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.fried_egg_backgroiund),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.size(150.dp)
            )
            LiquidBox(text = "Завтраки")
        }
    }
}
