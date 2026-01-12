package com.example.games_scoring_app

import android.net.Uri
import androidx.compose.ui.graphics.Color

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object Game : Screen("Game/{gameId}/{gameTypeId}"){
        fun createRoute(
            gameId: Int,
            gameTypeId: Int
        ): String {
            return "Game/$gameId/$gameTypeId"
        }
    }
    object SetUp : Screen("SetUp/{gameType}/{gameColor}?playerNames={playerNames}"){
        fun createRoute(gameType: Int?, gameColor: Color) = "SetUp/$gameType/${gameColor.value.toString(16)}"
        fun createRouteWithPlayers(gameType: Int, gameColor: Color, playerNames: List<String>): String {
            val names = playerNames.joinToString(",")
            return "SetUp/$gameType/${gameColor.value.toString(16)}?playerNames=$names"
        }
    }
    object SavedGames : Screen("SavedGames")
    object Settings : Screen("Settings")
    object Utilities : Screen("Utilities/{utilityId}"){
        fun createRoute(utilityId: Int) = "Utilities/$utilityId"
    }

}
