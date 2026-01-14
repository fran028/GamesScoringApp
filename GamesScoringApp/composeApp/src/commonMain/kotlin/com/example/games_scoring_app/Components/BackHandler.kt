package com.example.games_scoring_app.Components

import androidx.compose.runtime.Composable

@Composable
expect fun BindBackHandler(enabled: Boolean = true, onBack: () -> Unit)