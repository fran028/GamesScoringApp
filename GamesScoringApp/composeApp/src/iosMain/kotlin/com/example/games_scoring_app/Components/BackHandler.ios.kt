package com.example.games_scoring_app.Components

import androidx.compose.runtime.Composable

@Composable
actual fun BindBackHandler(enabled: Boolean, onBack: () -> Unit) {
    // iOS doesn't have a hardware back button, so this does nothing.
}