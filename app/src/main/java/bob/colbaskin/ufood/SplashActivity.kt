package bob.colbaskin.ufood

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import bob.colbaskin.ufood.common.design_system.theme.UfoodTheme

@SuppressLint("CustomSplashScreen")
class SplashActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UfoodTheme {
                SplashScreen()
            }
        }
    }
}