package bob.colbaskin.ufood.common.design_system.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

@Composable
fun UfoodTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkColorsScheme else lightColorsScheme
    val multiTypography = remember { createMultiTypography() }

    CompositionLocalProvider(
        localColors provides colorScheme,
        localMultiTypography provides multiTypography,
        content = content
    )
}

object CustomTheme {
    val colors: AppColors
        @Composable get() = localColors.current
    val typography: MultiTypography
        @Composable get() = localMultiTypography.current

}

private fun createMultiTypography() = MultiTypography(
    madeInfinity = createMaterial3Typography(MadeInfinityFontFamily),
    nunito = createMaterial3Typography(NunitoFontFamily),
    brightoWander = createMaterial3Typography(BrightoWanderFontFamily),
    gilroy = createMaterial3Typography(GilroyFontFamily),
    inter = createMaterial3Typography(InterFontFamily),
    adigianaUltra = createMaterial3Typography(AdigianaUltraFontFamily),
    airfool = createMaterial3Typography(AirfoolFontFamily),
    helvetica = createMaterial3Typography(HelveticaFontFamily)
)
