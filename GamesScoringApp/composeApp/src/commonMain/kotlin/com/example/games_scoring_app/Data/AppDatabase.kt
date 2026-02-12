package com.example.games_scoring_app.Data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.concurrent.Volatile

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>

@Database(
    entities = [Players::class, Games::class, Scores::class, GameTypes::class, Settings::class, ScoreTypes::class],
    version = 6,
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

        private val initializationMutex = Mutex()

        private val _isDatabaseReady = MutableStateFlow(false)
        val isDatabaseReady: StateFlow<Boolean> = _isDatabaseReady.asStateFlow()

        // Flag to prevent multiple concurrent initialization attempts
        private var isInitializing = false

        fun getDatabase(scope: CoroutineScope): AppDatabase {
            val existing = INSTANCE
            if (existing != null) {
                signalDatabaseOperational()
                return existing
            }

            val instance = getDatabaseBuilder()
                .setDriver(BundledSQLiteDriver())
                .addCallback(AppDatabaseCallback(scope))
                .build()

            INSTANCE = instance

            // Removed manual "Warm-up" query here to prevent race conditions.
            // Database will open when the first UI component collects from a DAO.

            // Safety fallback to unlock UI if database fails to signal ready
            scope.launch {
                kotlinx.coroutines.delay(5000)
                if (!_isDatabaseReady.value) {
                    signalDatabaseOperational()
                }
            }

            return instance
        }

        fun signalDatabaseOperational() {
            _isDatabaseReady.value = true
        }

        fun setInitializing(value: Boolean) {
            isInitializing = value
        }

        fun getIsInitializing(): Boolean = isInitializing
    }

    private class AppDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

        override fun onCreate(connection: SQLiteConnection) {
            super.onCreate(connection)
        }

        override fun onOpen(connection: SQLiteConnection) {
            super.onOpen(connection)

            // Check the static flag to ensure we only start one init coroutine
            if (!getIsInitializing()) {
                setInitializing(true)
                handleInitialization()
            }
        }

        private fun handleInitialization() {
            scope.launch(Dispatchers.IO) {
                // Use withLock to ensure only ONE coroutine enters this block at a time
                initializationMutex.withLock {
                    try {
                        val db = INSTANCE ?: return@launch

                        // Now this check is 100% thread-safe
                        populateDatabase(db.gameTypesDao(), db.settingsDao(), db.scoreTypesDao())

                        // Logging for verification
                        val gameTypes = db.gameTypesDao().getAllGameTypesAsList()
                        gameTypes.forEach {
                        }
                    } finally {
                        signalDatabaseOperational()
                    }
                }
            }
        }

        suspend fun populateDatabase(gameTypesDao: GameTypesDao, settingsDao: SettingsDao, scoreTypesDao: ScoreTypesDao) {
            val existing = gameTypesDao.getAllGameTypesAsList()

            if (existing.isEmpty()) {

                // Settings
                val settings = settingsDao.getSettings()
                if (settings.none { it.name == "theme" }) {
                    settingsDao.insertSettings(Settings(name = "theme", value = 0))
                }

                // Game Types
                val truco = gameTypesDao.getGameTypeByName("Truco")
                if(truco == null) {
                    val trucoId = gameTypesDao.insertGameType(
                        GameTypes(
                            name = "Truco",
                            description = "Juego de cartas popular en Argentina.",
                            maxPlayers = 2,
                            minPlayers = 2,
                            maxScore = 30,
                            type = "Cartas"
                        )
                    )
                    scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = trucoId.toInt(), name = "Final Score"))
                }

                val generala = gameTypesDao.getGameTypeByName("Generala")
                if(generala == null) {
                    val generalaId = gameTypesDao.insertGameType(
                        GameTypes(
                            name = "Generala",
                            description = "Juego de dados.",
                            maxPlayers = 8,
                            minPlayers = 1,
                            maxScore = 370,
                            type = "Dados"
                        )
                    )

                    listOf("1", "2", "3", "4", "5", "6", "Escalera", "Full", "Poker", "Generala", "Generala x2").forEach {
                        scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = it))
                    }
                }

                val points = gameTypesDao.getGameTypeByName("Points")
                if(points == null) {
                    val pointsId = gameTypesDao.insertGameType(
                        GameTypes(
                            name = "Points",
                            description = "Contar puntos hasta un objetivo.",
                            maxPlayers = 8,
                            minPlayers = 2,
                            maxScore = 1000,
                            type = "Generico"
                        )
                    )
                    scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = pointsId.toInt(), name = "Final Score"))
                }

                    val ranking = gameTypesDao.getGameTypeByName("Ranking")
                if(ranking == null) {
                    val rankingId = gameTypesDao.insertGameType(
                        GameTypes(
                            name = "Ranking",
                            description = "Rankea el resultado.",
                            maxPlayers = 8,
                            minPlayers = 3,
                            maxScore = 0,
                            type = "Generico"
                        )
                    )

                    scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = rankingId.toInt(), name = "Final Score"))
                }

                val levels = gameTypesDao.getGameTypeByName("Levels")
                if(levels == null) {
                    val levelsId = gameTypesDao.insertGameType(
                        GameTypes(
                            name = "Levels",
                            description = "Avanza niveles.",
                            maxPlayers = 8,
                            minPlayers = 1,
                            maxScore = 0,
                            type = "Generico"
                        )
                    )

                    scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = levelsId.toInt(), name = "Final Score"))
                }
            }
        }
    }
}