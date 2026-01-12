package com.example.games_scoring_app.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ScoresDao {

    /**
     * NEW: Inserts a single score entry into the database.
     * This will be used by the SetUp page to create the initial scores.
     * OnConflictStrategy.REPLACE is a safe default, though for initial scores,
     * it's unlikely to conflict.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(score: Scores)

    /**
     * Updates an existing score entry.
     * This is used by the game scoreboards when a player's score changes.
     */
    @Update
    suspend fun updateScore(score: Scores)

    @Delete
    suspend fun deleteScore(score: Scores)

    @Query("SELECT * FROM scores")
    fun getAllScores(): List<Scores>

    @Query("SELECT * FROM scores WHERE id = :id")
    fun getScoreById(id: Int): Scores?

    @Query("SELECT * FROM scores WHERE id_player = :id_player LIMIT 1")
    suspend fun getScoresByPlayerId(id_player: Int): Scores

}