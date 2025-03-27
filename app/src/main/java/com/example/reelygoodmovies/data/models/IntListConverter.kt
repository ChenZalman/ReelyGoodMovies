package com.example.reelygoodmovies.data.models

import androidx.room.TypeConverter

class IntListConverter {

    @TypeConverter
    fun fromIntList(list: List<Int>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun toIntList(data: String?): List<Int>? {
        return if (!data.isNullOrEmpty()) {
            data.split(",").mapNotNull { it.toIntOrNull() }
        } else {
            emptyList()
        }
    }
}