package com.exemple.sonicflow.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.exemple.sonicflow.data.model.Playlist
import com.exemple.sonicflow.data.model.PlaylistTrackCrossRef
import com.exemple.sonicflow.data.model.Song

@Database(
    entities = [Song::class, Playlist::class, PlaylistTrackCrossRef::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
}
