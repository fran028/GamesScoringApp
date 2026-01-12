package com.example.games_scoring_app.Data

data class GameStats(
    val timesPlayed: Int = 0,
    val daysSinceLastPlayed: String = "-" // Usamos String para manejar "N/A", "Hoy", etc.
)