package com.example.games_scoring_app.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow // Make sure this import is added

@Dao
interface GameTypesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameType(gameType: GameTypes): Long

    @Update
    suspend fun updateGameType(gameType: GameTypes) // Changed to suspend for consistency

    @Delete
    suspend fun deleteGameType(gameType: GameTypes) // Changed to suspend for consistency

    @Query("DELETE FROM gameTypes")
    suspend fun deleteGamesType()

    @Query("SELECT * FROM gameTypes ORDER BY name ASC")
    fun getAllGameTypes(): Flow<List<GameTypes>>

    @Query("SELECT count(*) as amount FROM gameTypes")
    fun getGameTypeAmount(): Int

    @Query("SELECT * FROM gameTypes WHERE id = :id")
    suspend fun getGameTypeById(id: Int): GameTypes?

    @Query("SELECT * FROM gameTypes WHERE name = :name")
    suspend fun getGameTypeByName(name: String): GameTypes? // Changed to suspend

    @Query("SELECT * FROM score_types WHERE id_game_type = :gameTypeId")
    suspend fun getScoreTypesByGameTypeIdAsList(gameTypeId: Int): List<ScoreTypes>

    @Query("SELECT * FROM gameTypes")
    suspend fun getAllGameTypesAsList(): List<GameTypes>
}
