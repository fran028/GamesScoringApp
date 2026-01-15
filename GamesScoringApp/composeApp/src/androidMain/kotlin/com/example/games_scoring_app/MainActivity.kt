package com.example.games_scoring_app
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen // Add this import

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // This must be called BEFORE super.onCreate()
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}