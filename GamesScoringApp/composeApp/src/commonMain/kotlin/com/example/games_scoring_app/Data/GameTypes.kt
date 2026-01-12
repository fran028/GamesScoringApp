package com.example.games_scoring_app.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gameTypes")
data class GameTypes(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "max_Players") val maxPlayers: Int,
    @ColumnInfo(name = "min_Players") val minPlayers: Int,
    @ColumnInfo(name = "max_Score") val maxScore: Int,
    @ColumnInfo(name = "min_Score") val minScore: Int = 0,
    @ColumnInfo(name = "type") val type: String = "",
    @ColumnInfo(name = "description") val description: String = "",
)