package com.example.games_scoring_app.Data

import androidx.room.Embedded
import androidx.room.Relation

/**
 * This data class is a container for holding a Player object and all of their associated Score objects.
 * Room uses this class to combine the results of a relational query into a single, clean object.
 * It's not an entity in the database, but a temporary structure for query results.
 */
data class PlayerWithScores(
    // The @Embedded annotation tells Room to treat the fields of the 'player' object
    // as if they were fields of this 'PlayerWithScores' class.
    @Embedded
    val player: Players,

    // The @Relation annotation defines the one-to-many relationship between Players and Scores.
    @Relation(
        parentColumn = "id",            // The primary key of the parent entity (Players.id).
        entityColumn = "id_player",     // The foreign key in the child entity (Scores.id_player).
        entity = Scores::class          // Specifies the child entity class.
    )
    val scores: List<Scores> // This will hold the list of all scores belonging to the player.
)
