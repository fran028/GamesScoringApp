package com.example.games_scoring_app.Viewmodel

// REMOVED: import android.icu.util.TimeUnit (This is for API 24+ and is likely the issue)
import android.os.Build
import androidx.annotation.RequiresApi
// REMOVED: import androidx.compose.ui.text.intl.Locale (This is the Compose-specific Locale)
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.games_scoring_app.Data.GameStats
import com.example.games_scoring_app.Data.GameWithPlayers
import com.example.games_scoring_app.Data.Games
import com.example.games_scoring_app.Data.GamesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
// --- ADD THIS IMPORT for SimpleDateFormat ---
import java.util.Locale
// --- ADD THIS IMPORT for TimeUnit ---
import java.util.concurrent.TimeUnit

class GamesViewModel(private val gamesRepository: GamesRepository) : ViewModel() {
    private val _lastGame = MutableStateFlow<Games?>(null)
    val lastGame: StateFlow<Games?> = _lastGame
    fun getLastGame() {
        viewModelScope.launch(Dispatchers.IO) {
            val games = gamesRepository.getLastGame() // Use the repository method
            withContext(Dispatchers.Main) {
                _lastGame.value = games
            }
        }
    }

    // --- NEW: StateFlow to hold games with players ---
    private val _allGamesWithPlayers = MutableStateFlow<List<GameWithPlayers>>(emptyList())
    val allGamesWithPlayers: StateFlow<List<GameWithPlayers>> = _allGamesWithPlayers.asStateFlow()

    // --- NEW: Function to fetch all games with their players ---
    fun getAllGamesWithPlayers() {
        viewModelScope.launch {
            gamesRepository.getAllGamesWithPlayers().collect { gamesList ->
                _allGamesWithPlayers.value = gamesList
            }
        }
    }

    private val _allGames = MutableStateFlow<List<Games?>>(listOf())
    val allGames: StateFlow<List<Games?>> = _allGames

    fun getAllGames(){
        viewModelScope.launch(Dispatchers.IO) {
            val gameList = gamesRepository.getAllGames()
            withContext(Dispatchers.Main) {
                _allGames.value = gameList
            }
        }
    }

    suspend fun addNewGame(game: Games): Long {
        return withContext(Dispatchers.IO) {
            gamesRepository.addNewGame(game)
        }
    }

    val _currentGame = MutableStateFlow<Games?>(null)
    val currentGame: StateFlow<Games?> = _currentGame

    fun getGameById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val game = gamesRepository.getGameById(id)
            withContext(Dispatchers.Main) {
                _currentGame.value = game
            }
        }
    }

    // --- NEW: Function to delete a game ---
    fun deleteGame(game: Games) = viewModelScope.launch { // <-- LAUNCH a coroutine here
        gamesRepository.deleteGame(game) // This now calls the suspend function correctly
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTodaysDate(): String {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return today.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun emptyGame(): Games{
        val date = this.getTodaysDate()
        val emptymatch = Games(
            id_GameType = 0,
            date = date
        )
        return emptymatch
    }

    private val _gameStats = MutableStateFlow<Map<Int, GameStats>>(emptyMap())
    val gameStats: StateFlow<Map<Int, GameStats>> = _gameStats

    fun getStatsForGameType(gameTypeId: Int) {
        viewModelScope.launch {
            val count = gamesRepository.getGamesCount(gameTypeId)
            val lastDateStr = gamesRepository.getLastPlayedDate(gameTypeId)
            val daysString = if (lastDateStr != null) {
                calculateDaysSince(lastDateStr)
            } else {
                "N/A" // No se ha jugado nunca
            }

            _gameStats.value = _gameStats.value + (gameTypeId to GameStats(count, daysString))
        }
    }

    private fun calculateDaysSince(dateStr: String): String {
        // This now correctly references java.util.Locale
        val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        return try {
            val lastDate = dateFormat.parse(dateStr)
            val currentDate = Date()
            val diffInMillis = currentDate.time - lastDate!!.time
            // This now correctly references java.util.concurrent.TimeUnit
            val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

            when {
                days == 0L -> "Hoy"
                days == 1L -> "Ayer"
                else -> "Hace $days días"
            }
        } catch (e: Exception) {
            "N/A" // En caso de error de formato
        }
    }
}
