package bob.colbaskin.cookly.common.design_system.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val background: Color,
    val accentColor: Color,
    val cardBackground: Color,
    val text: Color,
    val likeColor: Color,
    val flameColor: Color,
    val statsCardBackground: Color,
    val secondaryCardBackground: Color,
    val strokeColor: Color,
    val glassColor: Color,
    val outlinedStatsSurface: Color,
    val filledStatsSurface: Color,
    val mealCardBorder: Color,
    val bottomBarIcon: Color,
    val onErrorContainer: Color,
    val errorContainer: Color,
    val secondAccentColor: Color,
    val selectedPreferences: Color,
    val secondaryText: Color
)

val localColors = staticCompositionLocalOf { darkColorsScheme }

val darkColorsScheme = AppColors(
    background = Color(0xFFFFFFFF),
    accentColor = Color(0xFFE59830),
    cardBackground = Color(0xFF1C1B1C),
    text = Color(0xFF000000),
    likeColor = Color(0xFFFF5356),
    flameColor = Color(0xFFFF0000),
    statsCardBackground = Color(0xFFEDEDED),
    secondaryCardBackground = Color(0xFFF4F4F4),
    strokeColor = Color(0xFFE2E2E2),
    glassColor = Color(0xFFFFFFFF),
    outlinedStatsSurface = Color(0xFFF3F3F3),
    filledStatsSurface = Color(0xFFFFFFFF),
    mealCardBorder = Color(0xFF818181),
    bottomBarIcon = Color(0xFF9DB2CE),
    onErrorContainer = Color.White,
    errorContainer = Color.Red,
    secondAccentColor = Color(0xFF6F9448),
    selectedPreferences = Color(0xFFC7E5A6),
    secondaryText = Color(0xFFB9B9B9),
)

val lightColorsScheme = AppColors(
    background = Color(0xFFFFFFFF),
    accentColor = Color(0xFFE59830),
    cardBackground = Color(0xFF1C1B1C),
    text = Color(0xFF000000),
    likeColor = Color(0xFFFF5356),
    flameColor = Color(0xFFFF0000),
    statsCardBackground = Color(0xFFEDEDED),
    secondaryCardBackground = Color(0xFFF4F4F4),
    strokeColor = Color(0xFFE2E2E2),
    glassColor = Color(0xFFFFFFFF),
    outlinedStatsSurface = Color(0xFFF3F3F3),
    filledStatsSurface = Color(0xFFFFFFFF),
    mealCardBorder = Color(0xFF818181),
    bottomBarIcon = Color(0xFF9DB2CE),
    onErrorContainer = Color.White,
    errorContainer = Color.Red,
    secondAccentColor = Color(0xFF6F9448),
    selectedPreferences = Color(0xFFC7E5A6),
    secondaryText = Color(0xFFB9B9B9),
)
