package com.example.deviceinfogetter

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.deviceinfogetter.ui.screen.DeviceInfoScreen
import com.example.deviceinfogetter.ui.screen.SquareScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsets.Type.navigationBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setContent {
            DeviceInfoTheme {

                MainApp()
            }
        }
    }
}

@Composable
fun DeviceInfoTheme(content: @Composable () -> Unit) {

    MaterialTheme(
        colorScheme = lightColorScheme(),
        content = content
    )
}

@Composable
fun MainApp() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.DeviceInfo
    ) {

        composable<Screen.DeviceInfo> {
            DeviceInfoScreen(
                moveToSquareScreen = {
                    navController.navigate(Screen.Square)
                }
            )
        }

        composable<Screen.Square>{
            SquareScreen()
        }
    }
}