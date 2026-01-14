package com.example.games_scoring_app.Components

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun BindBackHandler(enabled: Boolean, onBack: () -> Unit) {
    BackHandler(enabled = enabled, onBack = onBack)
}