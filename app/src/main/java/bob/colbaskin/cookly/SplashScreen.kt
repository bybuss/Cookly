package bob.colbaskin.cookly

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.utils.calculateImageScaleToFullscreen

@Composable
fun SplashScreen() {

    val context = LocalContext.current
    val density = LocalDensity.current
    val screenPxWidth = with(density) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    val screenPxHeight = with(density) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }
    val center = with(density) {
        (screenPxWidth / 2).toDp()
    }
    val endScreen = with(density) {
        screenPxWidth.toDp()
    }

    var isAnimated by remember { mutableStateOf(false) }
    val burgerTransition = updateTransition(targetState = isAnimated)
    val burgerImage = ImageBitmap.imageResource(R.drawable.splash_main_logo)
    val imageHalfWidth = with(density) {
        (burgerImage.width / 2).toDp()
    }
    val burgerXOffset by burgerTransition.animateDp(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessVeryLow
            )
        }
    ) { state ->
        if (state) center - imageHalfWidth else -endScreen
    }
    val burgerSize = remember { Animatable(1f) }

    val avocadoOffset = remember {
        Animatable(Offset(0f, 0f), Offset.VectorConverter)
    }
    val smallCarrotOffset = remember {
        Animatable(Offset(0f, 0f), Offset.VectorConverter)
    }
    val blurredCarrotOffset = remember {
        Animatable(Offset(0f, 0f), Offset.VectorConverter)
    }

    val imageScale = calculateImageScaleToFullscreen(
        R.drawable.splash_main_logo,
        screenPxWidth,
        screenPxHeight
    )

    var isStartTransition by remember { mutableStateOf(false) }
    val transition = updateTransition(isStartTransition)
    val splashAlpha by transition.animateFloat(
        transitionSpec = { tween(300) },
        targetValueByState = { if (it) 0f else 1f }
    )

    LaunchedEffect(true) {
        launch {
            avocadoOffset.animateTo(
                targetValue = Offset(
                    x = (5..9).random().toFloat(),
                    y = (3..7).random().toFloat()),
                animationSpec = infiniteRepeatable(
                    animation = tween(3000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
        launch {
            smallCarrotOffset.animateTo(
                targetValue = Offset(
                    x = (-13..-8).random().toFloat(),
                    y = (9..14).random().toFloat()
                ),
                animationSpec = infiniteRepeatable(
                    animation = tween(3000, easing = LinearEasing, delayMillis = 75),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
        launch {
            blurredCarrotOffset.animateTo(
                targetValue = Offset(
                    x = (8..12).random().toFloat(),
                    y = (-6..-2).random().toFloat()
                ),
                animationSpec = infiniteRepeatable(
                    animation = tween(3000, easing = LinearEasing, delayMillis = 250),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }

        delay(1500)
        isAnimated = true
        delay(800)

        launch {
            burgerSize.animateTo(
                targetValue = imageScale,
                animationSpec = tween(600, easing = LinearEasing)
            )
        }

        delay(250)
        isStartTransition = true

        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
        (context as? Activity)?.overridePendingTransition(
            R.anim.zoom_fade_in,
            android.R.anim.fade_out
        )
        (context as? Activity)?.finish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = CustomTheme.colors.background)
            .graphicsLayer(alpha = splashAlpha)
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 150.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.splash_main_logo),
                contentDescription = "Splash screen main logo",
                modifier = Modifier.scale(1.8f)
            )
            Text(
                text = stringResource(R.string.app_name),
                style = CustomTheme.typography.madeInfinity.displayLarge,
                color = Color.White
            )
        }

        Image(
            painter = painterResource(R.drawable.blurred_avocado),
            contentDescription = "Blurred avocado image",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .zIndex(2f)
                .scale(1.55f)
                .offset(x = 20.dp, y = 32.dp)
                .offset {
                    IntOffset(
                        x = avocadoOffset.value.x.toInt(),
                        y = avocadoOffset.value.y.toInt()
                    )
                }
        )

        Box (modifier = Modifier.align(Alignment.BottomStart)) {
            Image(
                painter = painterResource(R.drawable.small_carrot),
                contentDescription = "Small carrot image",
                modifier = Modifier
                    .zIndex(2f)
                    .scale(1.1f)
                    .offset(x = 15.dp, y = (-20).dp)
                    .offset {
                        IntOffset(
                            x = smallCarrotOffset.value.x.toInt(),
                            y = smallCarrotOffset.value.y.toInt()
                        )
                    }
            )
            Image(
                painter = painterResource(R.drawable.blurred_carrot),
                contentDescription = "Blurred carrot image",
                modifier = Modifier
                    .zIndex(1f)
                    .scale(2f)
                    .offset {
                        IntOffset(
                            x = blurredCarrotOffset.value.x.toInt(),
                            y = blurredCarrotOffset.value.y.toInt()
                        )
                    }

            )
        }

        Image(
            painter = painterResource(R.drawable.splash_main_logo),
            contentDescription = "Burger",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = -burgerXOffset)
                .zIndex(3f)
                .scale(1.5f * burgerSize.value)
        )
    }
}
