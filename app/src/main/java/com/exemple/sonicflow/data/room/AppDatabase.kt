package com.exemple.sonicflow.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Playlist::class, PlaylistSong::class],
    version = 1,
    exportSchema = false // ✅ évite le warning et les crashs
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sonicflow_db"
                )
                    // ✅ tu peux ajouter .fallbackToDestructiveMigration() si tu veux éviter les crashs en cas de changement de schéma
                    .build().also { INSTANCE = it }
            }
        }
    }
}
