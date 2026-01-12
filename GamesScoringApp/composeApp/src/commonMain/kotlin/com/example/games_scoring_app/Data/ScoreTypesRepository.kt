package com.example.games_scoring_app.Data

import kotlinx.coroutines.flow.Flow

class ScoreTypesRepository(private val scoreTypesDao: ScoreTypesDao) {
    /**
     * Exposes the Flow from the DAO to be collected by the ViewModel for live updates.
     */
    fun getScoreTypesByGameTypeId(gameTypeId: Int): Flow<List<ScoreTypes>> {
        return scoreTypesDao.getScoreTypesByGameTypeId(gameTypeId)
    }

    /**
     * Exposes the suspend function from the DAO for one-shot data fetching.
     * This is called by the ViewModel for the SetUp page.
     */
    suspend fun getScoreTypesByGameTypeIdAsList(gameTypeId: Int): List<ScoreTypes> {
        return scoreTypesDao.getScoreTypesByGameTypeIdAsList(gameTypeId)
    }
}
