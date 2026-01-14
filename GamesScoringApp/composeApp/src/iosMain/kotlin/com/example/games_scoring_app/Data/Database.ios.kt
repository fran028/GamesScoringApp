package com.example.games_scoring_app.Data

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory
import androidx.room.RoomDatabaseConstructor

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = NSHomeDirectory() + "/games.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
        // factory must return the instance created by instantiateImpl()
        factory = { AppDatabaseConstructor.initialize() }
    )
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
actual object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    // This fixes "does not implement abstract member initialize"
    // The Room compiler plugin will substitute the body of this function
    override fun initialize(): AppDatabase = error("Should be substituted by Room compiler")
}