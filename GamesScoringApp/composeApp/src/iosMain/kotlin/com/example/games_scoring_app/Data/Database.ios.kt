package com.example.games_scoring_app.Data

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import platform.Foundation.NSHomeDirectory

// This satisfies the "expect fun getDatabaseBuilder"
actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = NSHomeDirectory() + "/games.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
        factory = { AppDatabase::class.instantiateImpl() }
    )
}

// This satisfies the "expect object AppDatabaseConstructor"
// Note: Room's Gradle plugin generates the implementation, but we need
// the actual declaration to link against the expect in commonMain.
@Suppress("NO_ACTUAL_FOR_EXPECT")
actual object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>