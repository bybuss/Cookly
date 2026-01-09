package bob.colbaskin.ufood.utils

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource

@Composable
fun calculateImageScaleToFullscreen(
    @DrawableRes image: Int,
    screenWidth: Float,
    screenHeight: Float
): Float {
    val imageBitmap = ImageBitmap.imageResource(image)
    val imageWidth = imageBitmap.width
    val imageHeight = imageBitmap.height
    val scaleX = screenWidth / imageWidth
    val scaleY = screenHeight / imageHeight

    return maxOf(scaleX, scaleY)
}