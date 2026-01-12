package com.example.games_scoring_app.Data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreTypesDao {
    /**
     * Inserts a new score type into the database.
     * If a score type with the same ID already exists, it will be ignored,
     * which is useful for pre-populating data without causing crashes on re-runs.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertScoreType(scoreType: ScoreTypes)

    /**
     * Fetches all score types for a given game type ID as a reactive Flow.
     * This is used for observing live changes in the game screen.
     */
    @Query("SELECT * FROM score_types WHERE id_game_type = :gameTypeId")
    fun getScoreTypesByGameTypeId(gameTypeId: Int): Flow<List<ScoreTypes>>

    /**
     * Fetches all score types for a given game type ID as a simple List.
     * This is a one-shot operation, perfect for the SetUp page logic.
     */
    @Query("SELECT * FROM score_types WHERE id_game_type = :gameTypeId")
    suspend fun getScoreTypesByGameTypeIdAsList(gameTypeId: Int): List<ScoreTypes>


}
