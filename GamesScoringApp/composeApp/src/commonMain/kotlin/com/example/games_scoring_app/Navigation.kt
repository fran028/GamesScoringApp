package com.example.games_scoring_app

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb // Important import
import kotlinx.serialization.Serializable
import androidx.compose.ui.graphics.toArgb

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    object Home : Screen("Home")

    @Serializable
    object Game : Screen("Game/{gameId}/{gameTypeId}") {
        fun createRoute(
            gameId: Int,
            gameTypeId: Int
        ): String {
            return "Game/$gameId/$gameTypeId"
        }
    }

    @Serializable
    object SetUp : Screen("SetUp/{gameType}/{gameColor}?playerNames={playerNames}") {
        // FIX: Use toArgb() and toUInt() to get a clean 8-character hex string (e.g., "ff7da8e8")
        fun createRoute(gameType: Int?, gameColor: Color): String {
            val colorHex = gameColor.toArgb().toUInt().toString(16)
            return "SetUp/$gameType/$colorHex"
        }

        fun createRouteWithPlayers(gameType: Int, gameColor: Color, playerNames: List<String>): String {
            val names = playerNames.joinToString(",")
            val colorHex = gameColor.toArgb().toUInt().toString(16)
            return "SetUp/$gameType/$colorHex?playerNames=$names"
        }
    }

    @Serializable
    object SavedGames : Screen("SavedGames")

    @Serializable
    object Settings : Screen("Settings")

    @Serializable
    object Utilities : Screen("Utilities/{utilityId}") {
        fun createRoute(utilityId: Int) = "Utilities/$utilityId"
    }
}