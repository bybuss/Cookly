package bob.colbaskin.cookly.common.design_system.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val background: Color,
    val primaryButton: Color,
    val authButton: Color,
    val buyButton: Color,
    val sendButton: Color,
    val cardSurface: Color,
    val dishOfDayCardBackground: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val text: Color,
    val filledStatsSurface: Color,
    val outlinedStatsSurface: Color,
    val flameIconColor: Color,
    val mealCardBorder: Color
)

val localColors = staticCompositionLocalOf { darkColorsScheme }

val darkColorsScheme = AppColors(
    background = Color(0xFF252830),
    primaryButton = Color(0xFF575C6A),
    authButton = Color(0xFFFE8745),
    buyButton = Color(0xFFB1FF9C),
    sendButton = Color(0xFFFFFFFF),
    cardSurface = Color(0xFF292C34),
    dishOfDayCardBackground = Color(0xFFB9480D),
    errorContainer = Color.Red,
    onErrorContainer = Color.White,
    text = Color(0xFFFFFFFF),
    filledStatsSurface = Color(0xFFFFFFFF),
    outlinedStatsSurface = Color(0xFFF3F3F3),
    flameIconColor = Color(0xFFFF0000),
    mealCardBorder = Color(0xFF818181)
)

val lightColorsScheme = AppColors(
    background = Color(0xFFADADA6),
    primaryButton = Color(0xFF575C6A),
    authButton = Color(0xFFFE8745),
    buyButton = Color(0xFFB1FF9C),
    sendButton = Color(0xFFFFFFFF),
    cardSurface = Color(0xFF292C34),
    dishOfDayCardBackground = Color(0xFFB9480D),
    errorContainer = Color.Red,
    onErrorContainer = Color.White,
    text = Color(0xFF000000),
    filledStatsSurface = Color(0xFFFFFFFF),
    outlinedStatsSurface = Color(0xFFF3F3F3),
    flameIconColor = Color(0xFFFF0000),
    mealCardBorder = Color(0xFF818181)
)
