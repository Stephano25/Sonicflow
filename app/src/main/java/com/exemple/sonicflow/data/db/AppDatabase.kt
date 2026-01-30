package com.exemple.sonicflow.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.exemple.sonicflow.data.model.Playlist
import com.exemple.sonicflow.data.model.PlaylistTrackCrossRef
import com.exemple.sonicflow.data.model.Song
import com.exemple.sonicflow.data.model.WaveformData

@Database(
    entities = [Song::class, Playlist::class, PlaylistTrackCrossRef::class, WaveformData::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun waveformDao(): WaveformDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sonicflow.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
