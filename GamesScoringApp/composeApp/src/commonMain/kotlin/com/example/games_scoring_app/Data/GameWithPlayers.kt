package com.example.games_scoring_app.Data

import androidx.room.Embedded
import androidx.room.Relation

data class GameWithPlayers(
    @Embedded
    val game: Games,

    @Relation(
        parentColumn = "id", // The primary key of the parent entity (Games)
        entityColumn = "id_game"  // The foreign key in the child entity (Player)
    )
    val players: List<Players>
)
