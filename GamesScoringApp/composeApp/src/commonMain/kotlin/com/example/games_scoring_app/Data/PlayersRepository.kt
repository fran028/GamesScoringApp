package com.example.games_scoring_app.Data

import kotlinx.coroutines.flow.Flow

class PlayersRepository(private val playersDao: PlayersDao) {
    /**
     * MODIFIED: This function is now a suspend function that returns the ID (Long)
     * of the player that was just inserted. This is crucial for creating related score entries.
     */
    suspend fun insertPlayer(player: Players): Long {
        return playersDao.insertPlayer(player)
    }

    // This function is correct. It's a suspend function for updating.
    suspend fun updatePlayer(player: Players) {
        playersDao.updatePlayer(player)
    }

    // This function is correct. It's a suspend function for deleting.
    suspend fun deletePlayer(player: Players) {
        playersDao.deletePlayer(player)
    }

    // This function is correct. It's a suspend function for getting all players.
    suspend fun getAllPlayers(): List<Players> {
        return playersDao.getAllPlayers()
    }

    // This function is correct. It's a suspend function for getting a single player by ID.
    suspend fun getPlayerById(id: Int): Players? {
        return playersDao.getPlayerById(id)
    }

    // This function is correct. It's a suspend function for getting a player by name.
    suspend fun getPlayerByName(name: String): Players? {
        return playersDao.getPlayerByName(name)
    }

    /**
     * NEW: Exposes the DAO function to get players along with their associated scores.
     * This is the primary function that Game.kt will use to get its data.
     */
    fun getPlayersWithScores(gameId: Int): Flow<List<PlayerWithScores>> {
        return playersDao.getPlayersWithScores(gameId)
    }

    /**
     * This function is kept for scenarios where only the list of players is needed,
     * without their scores.
     */
    suspend fun getPlayersByGameIdAsList(gameId: Int): List<Players> {
        return playersDao.getPlayersByGameIdAsList(gameId)
    }

    /*
     * This function is now redundant for Game.kt, as it doesn't provide the scores.
     * It's commented out but can be removed.
    fun getPlayersByGameId(gameId: Int): Flow<List<Players>> {
        return playersDao.getPlayersByGameId(gameId)
    }
    */
}
