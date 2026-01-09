package bob.colbaskin.ufood

import android.content.res.Configuration
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import bob.colbaskin.ufood.common.design_system.theme.CustomTheme
import bob.colbaskin.ufood.common.design_system.theme.UfoodTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val isDakTheme = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        setupSystemBar(window, isDakTheme)

        setContent {
            UfoodTheme {
                Box(modifier = Modifier.fillMaxSize().background(CustomTheme.colors.background)) {}
            }
        }
    }
}


private fun setupSystemBar(window: Window, isDarkTheme: Boolean) {
    WindowCompat.setDecorFitsSystemWindows(window, false)

    val controller =  WindowCompat.getInsetsController(window, window.decorView)
    controller.isAppearanceLightStatusBars = !isDarkTheme
}
