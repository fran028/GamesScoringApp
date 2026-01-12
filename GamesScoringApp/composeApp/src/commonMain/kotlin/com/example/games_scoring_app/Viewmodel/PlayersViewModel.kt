package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.games_scoring_app.Data.PlayerWithScores
import com.example.games_scoring_app.Data.Players
import com.example.games_scoring_app.Data.PlayersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayersViewModel(private val playersRepository: PlayersRepository) : ViewModel() {

    /**
     * MODIFIED: This is now a suspend function that returns the ID of the new player.
     * This is essential for creating related score entries.
     */
    suspend fun addNewPlayer(player: Players): Long {
        return withContext(Dispatchers.IO) {
            playersRepository.insertPlayer(player)
        }
    }

    /**
     * NEW: Returns a Flow that emits a list of players, each with their associated scores.
     * The UI (Game.kt) will collect this Flow to get real-time updates for both players and scores.
     */
    fun getPlayersWithScores(gameId: Int): Flow<List<PlayerWithScores>> {
        return playersRepository.getPlayersWithScores(gameId)
    }

    /**
     * Updates an existing player in the database.
     * This is useful for changing a player's name, but not for scores.
     */
    suspend fun updatePlayer(player: Players) {
        withContext(Dispatchers.IO) {
            playersRepository.updatePlayer(player)
        }
    }

    /**
     * This function is kept for cases where you might only need the player list without their scores.
     */
    suspend fun fetchPlayersByGameId(id: Int): List<Players> {
        return playersRepository.getPlayersByGameIdAsList(id)
    }

    /*
     * The old getPlayersByGameId function is no longer needed as Game.kt now requires
     * the more complex PlayerWithScores object.
    fun getPlayersByGameId(gameId: Int): Flow<List<Players>> {
        return playersRepository.getPlayersByGameId(gameId)
    }
    */
}
