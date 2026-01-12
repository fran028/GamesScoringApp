package com.example.games_scoring_app.Data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// FIXME: Add actual expects for logger/log if Android Log is not available in commonMain, or use println
// For now removing Android Log import might break logging unless handled.
// Using println or expect/actual logger is recommended for KMP. I will comment out Log calls in next steps or assume user fixes it.
// Actually I'll keep Log calls but I need to handle the import. "android.util.Log" is not commonMain.
// I will remove the import and comment out Logs? Or replace with println?
// I'll replace with println for now or leave it if I can't easily fix.
// Wait, I can't use `android.util.Log` in commonMain.
// I will replace imports.

import androidx.room.RoomDatabaseConstructor

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>

@Database(
    entities = [Players::class, Games::class, Scores::class, GameTypes::class, Settings::class, ScoreTypes::class],
    version = 5,
    exportSchema = true,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playersDao(): PlayersDao
    abstract fun gamesDao(): GamesDao
    abstract fun scoresDao(): ScoresDao
    abstract fun gameTypesDao(): GameTypesDao
    abstract fun settingsDao(): SettingsDao
    abstract fun scoreTypesDao(): ScoreTypesDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val _isDatabaseReady = MutableStateFlow(false)
        val isDatabaseReady: StateFlow<Boolean> = _isDatabaseReady.asStateFlow()

        fun getDatabase(scope: CoroutineScope): AppDatabase {
            println("AppDatabase: getDatabase called")
            if(INSTANCE == null){
                println("AppDatabase: Creating new database instance")
                val builder = getDatabaseBuilder()
                val instance = builder
                    .setDriver(androidx.sqlite.driver.bundled.BundledSQLiteDriver())
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
            }
            println("AppDatabase: Returning existing database instance")
            signalDatabaseOperational()
            return INSTANCE!!
        }
        private fun signalDatabaseOperational() {
            if (!_isDatabaseReady.value) { // Only set to true if it's currently false
                _isDatabaseReady.value = true
                println("AppDatabase: Database signaled as operational (created/opened and populated if new).")
            }
        }

        private fun stopSignalDatabaseOperational() {
            if (_isDatabaseReady.value) { // Only set to false if it's currently true
                _isDatabaseReady.value = false
                println("AppDatabase: Database signaled as operational (created/opened and populated if new).")
            }
        }
    }
    private class AppDatabaseCallback(private val scope: CoroutineScope) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            println("AppDatabase: onCreate called")
            super.onCreate(db)

        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            println("AppDatabase: onOpen called")
            super.onOpen(db)
            scope.launch(Dispatchers.IO) {
                val database = INSTANCE ?: AppDatabase.getDatabase(scope)
                stopSignalDatabaseOperational()
                println("AppDatabase: Pre-populating database...")
                // --- CHANGE 1: Pass the new DAO here ---
                populateDatabase(database.gameTypesDao(), database.settingsDao(), database.scoreTypesDao())
                println("AppDatabase: Database pre-populated.")
                signalDatabaseOperational()
            }

        }


        // --- CHANGE 2: Add the ScoreTypesDao parameter ---
        suspend fun populateDatabase(gameTypesDao: GameTypesDao, settingsDao: SettingsDao, scoreTypesDao: ScoreTypesDao) {

            // Only clear and populate if the tables are empty to avoid wiping data on every open
            if (gameTypesDao.getAllGameTypesAsList().isEmpty()) {
                println("AppDatabase: GameTypes table is empty, populating...")

                val settings = settingsDao.getSettings()

                if (settings.isEmpty()) {
                    settingsDao.insertSettings(Settings(name = "theme", value = 0))
                } else {
                    var themeModeLoaded = false
                    for (setting in settings) {
                        if (setting.name == "theme") {
                            themeModeLoaded = true
                            break
                        }
                    }

                    if (!themeModeLoaded) {
                        settingsDao.insertSettings(Settings(name = "theme", value = 0))
                    }
                }



                // Add your initial data here using your DAO
                println("AppDatabase: Adding initial data to database...")
                // Note: The insert function needs to return the ID to link the score types
                val trucoId = gameTypesDao.insertGameType(GameTypes(name = "Truco", description = "Juego de cartas popular en Argentina.", maxPlayers = 2, minPlayers = 2, maxScore = 30, type = "Cartas"))
                val generalaId = gameTypesDao.insertGameType(GameTypes(name = "Generala", description = "Juego de dados, el objetivo es lograr las mejores combinaciones.", maxPlayers = 8, minPlayers = 1, maxScore = 370, type = "Dados"))
                val pointsId = gameTypesDao.insertGameType(GameTypes(name = "Points", description = "Modo genérico para contar puntos hasta un objetivo.", maxPlayers = 8, minPlayers = 2, maxScore = 1000, type = "Generico"))
                val rankingId = gameTypesDao.insertGameType(GameTypes(name = "Ranking", description = "Rankea el resultado de un juego o ronda.", maxPlayers = 8, minPlayers = 3, maxScore = 0, type = "Generico"))
                val levelsId = gameTypesDao.insertGameType(GameTypes(name = "Levels", description = "Avanza a través de niveles o rondas, contando tu progreso.", maxPlayers = 8, minPlayers = 1, maxScore = 0, type = "Generico"))


                // --- CHANGE 3: Populate the ScoreTypes table ---
                println("AppDatabase: Populating ScoreTypes...")
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = trucoId.toInt(), name = "Final Score"))

                // Generala Score Types
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "1"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "2"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "3"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "4"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "5"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "6"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "Escalera"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "Full"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "Poker"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "Generala"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "Generala x2"))

                // Generic Score Types
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = pointsId.toInt(), name = "Final Score"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = rankingId.toInt(), name = "Final Score"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = levelsId.toInt(), name = "Final Score"))

                println("AppDatabase: ScoreTypes populated.")
            } else {
                println("AppDatabase: Database already populated.")
            }
        }
    }
}
