package com.example.games_scoring_app.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "games")
data class Games(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "id_GameType") var id_GameType: Int,
    @ColumnInfo(name = "date") val date: String = getTodaysDate(),
)

// Helper function to get the current date as a formatted string
private fun getTodaysDate(): String {
    // This pattern matches the "dd/MM/yyyy" format you were using
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    // Gets the current date and formats it
    return formatter.format(Date())
}
