package com.example.games_scoring_app.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class Scores(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "id_player") val id_player: Int,
    @ColumnInfo(name = "score") var score: Int,
    @ColumnInfo(name = "isTotalScore") val isFinalScore: Boolean,
    @ColumnInfo(name = "id_score_type") val id_score_type: Int
)