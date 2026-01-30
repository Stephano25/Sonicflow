package com.exemple.sonicflow.data.db

import android.net.Uri
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromUri(uri: Uri?): String? = uri?.toString()

    @TypeConverter
    fun toUri(uriString: String?): Uri? = uriString?.let { Uri.parse(it) }

    @TypeConverter
    fun fromIntList(list: List<Int>?): String? = list?.joinToString(",")

    @TypeConverter
    fun toIntList(data: String?): List<Int>? =
        data?.split(",")?.mapNotNull { it.toIntOrNull() }
}
