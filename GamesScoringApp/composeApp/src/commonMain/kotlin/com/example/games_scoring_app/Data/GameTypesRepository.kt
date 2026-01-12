package com.example.games_scoring_app.Data

import kotlinx.coroutines.flow.Flow

class GameTypesRepository(private val gameTypesDao: GameTypesDao) {

    /**
     * MODIFIED: This is now a suspend function that returns the ID of the new game type.
     * This is crucial for linking the initial score types in the database callback.
     */
    suspend fun insertGameType(gameType: GameTypes): Long {
        return gameTypesDao.insertGameType(gameType)
    }

    /**
     * MODIFIED: This is now a suspend function for asynchronous updates.
     */
    suspend fun updateGameType(gameType: GameTypes) {
        gameTypesDao.updateGameType(gameType)
    }

    /**
     * MODIFIED: This is now a suspend function for asynchronous deletion.
     */
    suspend fun deleteGameType(gameType: GameTypes) {
        gameTypesDao.deleteGameType(gameType)
    }

    /**
     * MODIFIED: This now returns a Flow to observe live data changes.
     * The ViewModel will collect this Flow.
     */
    fun getAllGameTypes(): Flow<List<GameTypes>> {
        return gameTypesDao.getAllGameTypes()
    }

    /**
     * This function is correct. It's a one-shot suspend function to get a single game type.
     */
    suspend fun getGameTypeById(id: Int): GameTypes? {
        return gameTypesDao.getGameTypeById(id)
    }

    /**
     * MODIFIED: This is now a suspend function for asynchronous fetching.
     */
    suspend fun getGameTypeByName(name: String): GameTypes? {
        return gameTypesDao.getGameTypeByName(name)
    }

    /**
     * NEW: Exposes the DAO function to get the score types associated with a game type.
     * This is needed in SetUp.kt to create the initial empty scores for players.
     */
    suspend fun getScoreTypesByGameTypeIdAsList(gameTypeId: Int): List<ScoreTypes> {
        return gameTypesDao.getScoreTypesByGameTypeIdAsList(gameTypeId)
    }
}
