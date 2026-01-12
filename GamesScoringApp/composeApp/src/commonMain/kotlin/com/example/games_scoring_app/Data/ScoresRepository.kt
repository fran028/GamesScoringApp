package com.example.games_scoring_app.Data

class ScoresRepository(private val scoresDao: ScoresDao) {

    /**
     * NEW: A suspend function to insert a score via the DAO.
     * This will be called from the ScoresViewModel.
     */
    suspend fun insertScore(score: Scores) {
        scoresDao.insertScore(score)
    }

    /**
     * A suspend function to update a score via the DAO.
     */
    suspend fun updateScore(score: Scores) {
        scoresDao.updateScore(score)
    }

    suspend fun deleteScore(score: Scores) {
        scoresDao.deleteScore(score)
    }

    fun getAllScores(): List<Scores> {
        return scoresDao.getAllScores()
    }

    fun getScoreById(id: Int): Scores? {
        return scoresDao.getScoreById(id)
    }

    suspend fun getScoresByPlayerId(id_player: Int): Scores {
        return scoresDao.getScoresByPlayerId(id_player)
    }

}