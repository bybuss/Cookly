package bob.colbaskin.ufood.common.design_system.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import bob.colbaskin.ufood.R

data class AppTypography(
    val displayLarge: TextStyle,
    val displayMedium: TextStyle,
    val displaySmall: TextStyle,
    val headlineLarge: TextStyle,
    val headlineMedium: TextStyle,
    val headlineSmall: TextStyle,
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
    val labelLarge: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle
)

data class MultiTypography(
    val madeInfinity: AppTypography,
    val nunito: AppTypography,
    val brightoWander: AppTypography,
    val gilroy: AppTypography,
    val inter: AppTypography,
    val adigianaUltra: AppTypography,
    val airfool: AppTypography,
    val helvetica: AppTypography
)

data class TypographyWeights(
    val displayWeight: FontWeight = FontWeight.Bold,
    val headlineWeight: FontWeight = FontWeight.Bold,
    val titleWeight: FontWeight = FontWeight.Medium,
    val bodyWeight: FontWeight = FontWeight.Normal,
    val labelWeight: FontWeight = FontWeight.Normal
)

fun createMaterial3Typography(
    fontFamily: FontFamily,
    weights: TypographyWeights = TypographyWeights(),
) = AppTypography(
    displayLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = weights.displayWeight,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = weights.displayWeight,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = weights.displayWeight,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = weights.headlineWeight,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = weights.headlineWeight,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = weights.headlineWeight,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = weights.titleWeight,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = weights.titleWeight,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = weights.titleWeight,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = weights.bodyWeight,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = weights.bodyWeight,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = weights.bodyWeight,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
    labelLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = weights.labelWeight,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    labelMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = weights.labelWeight,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = weights.labelWeight,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    )
)

val MadeInfinityFontFamily = FontFamily(
    Font(R.font.made_infinity_black, FontWeight.Black)
)

val NunitoFontFamily = FontFamily(
    Font(R.font.nunito_bold, FontWeight.Bold),
    Font(R.font.nunito_medium, FontWeight.Medium),
    Font(R.font.nunito_regular, FontWeight.Normal)
)

val BrightoWanderFontFamily = FontFamily(
    Font(R.font.brighto_wander_regular, FontWeight.Normal)
)

val GilroyFontFamily = FontFamily(
    Font(R.font.gilroy_bold, FontWeight.Bold),
    Font(R.font.gilroy_medium, FontWeight.Medium),
    Font(R.font.gilroy_regular, FontWeight.Normal)
)

val InterFontFamily = FontFamily(
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_light, FontWeight.Light),
    Font(R.font.inter_thin_italic, FontWeight.Thin, style = FontStyle.Italic),
)

val AdigianaUltraFontFamily = FontFamily(
    Font(R.font.adigiana_ultra_regular, FontWeight.Normal),
)

val AirfoolFontFamily = FontFamily(
    Font(R.font.airfool_regular, FontWeight.Normal),
)
val HelveticaFontFamily = FontFamily(
    Font(R.font.helvetica_regular, FontWeight.Normal)
)

val localMultiTypography = staticCompositionLocalOf<MultiTypography> {
    error("No typography provided")
}
