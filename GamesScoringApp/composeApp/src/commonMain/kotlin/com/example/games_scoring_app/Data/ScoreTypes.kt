package com.example.games_scoring_app.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_types")
data class ScoreTypes(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "id_game_type") val id_game_type: Int,
    @ColumnInfo(name = "name") val name: String
)

