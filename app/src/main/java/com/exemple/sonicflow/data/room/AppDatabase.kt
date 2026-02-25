package com.exemple.sonicflow.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Playlist::class, PlaylistSong::class],
    version = 2,  // INC RÉMENTER LA VERSION
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        // Migration de la version 1 à 2
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Ajouter la colonne albumId si elle n'existe pas
                db.execSQL("ALTER TABLE playlist_songs ADD COLUMN albumId INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sonicflow_db"
                )
                    .addMigrations(MIGRATION_1_2)  // Ajouter la migration
                    .fallbackToDestructiveMigration()  // En cas d'erreur, recréer la DB
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}