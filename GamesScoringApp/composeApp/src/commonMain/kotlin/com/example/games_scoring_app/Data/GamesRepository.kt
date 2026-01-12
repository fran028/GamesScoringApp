package com.example.games_scoring_app.Data

import kotlinx.coroutines.flow.Flow

class GamesRepository(private val gamesDao: GamesDao) {
    fun insertGame(game: Games) {
        gamesDao.insertGame(game)
    }

    suspend fun addNewGame(game: Games): Long {
        return gamesDao.addNewGame(game)
    }


    fun updateGame(game: Games) {
        gamesDao.updateGame(game)
    }

    suspend fun deleteGame(game: Games) {
        gamesDao.deleteGame(game)
    }

    fun getAllGames(): List<Games> {
        return gamesDao.getAllGames()
    }

    // This is the new function to get all games with their players
    fun getAllGamesWithPlayers(): Flow<List<GameWithPlayers>> {
        return gamesDao.getAllGamesWithPlayers()
    }

    fun getLastGame(): Games? {
        return gamesDao.getLastGame()
    }

    fun getGameById(id: Int): Games? {
        return gamesDao.getGameById(id)
    }

    suspend fun getGamesCount(gameTypeId: Int): Int {
        return gamesDao.getGamesCountByGameType(gameTypeId)
    }

    suspend fun getLastPlayedDate(gameTypeId: Int): String? {
        return gamesDao.getLastPlayedDateByGameType(gameTypeId)
    }

}
