package com.example.games_scoring_app.Data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayersDao {
    /**
     * MODIFIED: This function now returns the 'id' of the inserted player as a Long.
     * This is essential for linking scores to this new player.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: Players): Long

    @Update
    suspend fun updatePlayer(player: Players)

    @Delete
    suspend fun deletePlayer(player: Players)

    @Query("SELECT * FROM Players") // Assuming table name is players_table
    suspend fun getAllPlayers(): List<Players>

    @Query("SELECT * FROM Players WHERE id = :id")
    suspend fun getPlayerById(id: Int): Players?

    @Query("SELECT * FROM Players WHERE name = :name")
    suspend fun getPlayerByName(name: String): Players?

    /**
     * NEW: This is the primary function for the GamePage.
     * The @Transaction annotation is crucial as it runs the two underlying queries
     * (one for the Player and one for the Scores) as a single atomic operation.
     * Room uses the @Relation in the PlayerWithScores class to assemble the results.
     */
    @Transaction
    @Query("SELECT * FROM Players WHERE id_game = :gameId")
    fun getPlayersWithScores(gameId: Int): Flow<List<PlayerWithScores>>

    /**
     * This function remains for cases where only the player list is needed without scores.
     */
    @Query("SELECT * FROM Players WHERE id_game = :gameId")
    suspend fun getPlayersByGameIdAsList(gameId: Int): List<Players>

    /*
     * This function is now redundant for Game.kt but kept for compatibility.
     * getPlayersWithScores should be used instead.
     */
    @Query("SELECT * FROM Players WHERE id_Game = :gameId")
    fun getPlayersByGameId(gameId: Int): Flow<List<Players>>
}
