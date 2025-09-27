package com.calyrsoft.ucbp1.features.movies.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.calyrsoft.ucbp1.features.movies.data.database.entity.MovieEntity
import com.calyrsoft.ucbp1.features.movies.data.database.entity.MovieLikeEntity

@Database(
    entities = [MovieEntity::class, MovieLikeEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun movieDao(): IMovieDao
    abstract fun movieLikeDao(): IMovieLikeDao

    companion object {
        @Volatile
        private var Instance: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppRoomDatabase::class.java,
                    "movies_db_entregables"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { Instance = it }
            }
        }

        // 👇 Migración para crear la tabla de likes
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS movie_likes(
                        movieId INTEGER NOT NULL PRIMARY KEY,
                        liked INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
    }
}
