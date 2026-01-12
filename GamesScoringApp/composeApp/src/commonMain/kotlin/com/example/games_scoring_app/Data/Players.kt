package com.example.games_scoring_app.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This entity correctly represents a single player in a single game.
 * It does not need any changes. The player's score is handled separately
 * in the 'Scores' table and joined using the 'PlayerWithScores' data class.
 */
@Entity(tableName = "players") // This table name should be used in your DAO queries
data class Players(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "won") val won: Boolean = false,
    @ColumnInfo(name = "id_game") var id_game: Int,
)
