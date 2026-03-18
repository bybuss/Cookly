package bob.colbaskin.cookly

import android.content.res.Configuration
import android.os.Bundle
import android.view.Window
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import bob.colbaskin.cookly.common.MainViewModel
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.common.user_prefs.data.models.UserPreferences
import bob.colbaskin.cookly.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val isDakTheme = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        setupSystemBar(window, isDakTheme)
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)

        var uiState: UiState<UserPreferences> by mutableStateOf(UiState.Loading)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.uiState
                    .onEach { uiState = it }
                    .collect()
            }
        }

        setContent {
            SideEffect {
                insetsController.isAppearanceLightStatusBars = !isDakTheme
            }

            if (uiState is UiState.Success<UserPreferences>) {
                UfoodTheme {
                    AppNavHost(uiState = uiState as UiState.Success<UserPreferences>)
                }
            }
        }
    }
}


private fun setupSystemBar(window: Window, isDarkTheme: Boolean) {
    WindowCompat.setDecorFitsSystemWindows(window, false)

    val controller =  WindowCompat.getInsetsController(window, window.decorView)
    controller.isAppearanceLightStatusBars = !isDarkTheme
}
