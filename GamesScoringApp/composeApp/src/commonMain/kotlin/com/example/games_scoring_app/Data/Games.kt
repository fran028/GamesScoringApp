package com.example.games_scoring_app.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.text.padStart

@Entity(tableName = "games")
data class Games(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "id_GameType") var id_GameType: Int,
    @ColumnInfo(name = "date") val date: String = getTodaysDate(),
)

private fun getTodaysDate(): String {
    // This now uses kotlinx.datetime.Clock.System
    val now = kotlinx.datetime.Clock.System.now()

    // This will now correctly find the extension function
    val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())

    // Note: Use .dayOfMonth instead of .day (which is the DayOfWeek object)
    val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
    val month = localDateTime.monthNumber.toString().padStart(2, '0')
    val year = localDateTime.year

    return "$day/$month/$year"
}