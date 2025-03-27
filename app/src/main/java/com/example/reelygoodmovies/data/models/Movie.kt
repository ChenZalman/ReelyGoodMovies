package com.example.reelygoodmovies.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName


@Entity(tableName = "movies")
data class Movie(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "plot")
    @SerializedName("overview")
    val plot: String,
    @ColumnInfo(name = "length")
    val length: Int = 0,
    @ColumnInfo(name = "year")
    val year: Int = 0,
    @ColumnInfo(name = "rate")
    @SerializedName("vote_average")
    val rate: Float,
    @TypeConverters(IntListConverter::class)
    @ColumnInfo(name = "genre")
    @SerializedName("genre_ids")
    var genre: List<Int> = emptyList(),
    @ColumnInfo(name = "photo")
    val photo: String?,
    @ColumnInfo(name = "favorite")
    var favorite: Boolean = false,
    @ColumnInfo(name = "localGen")
    var localGen: Boolean = false,
    @ColumnInfo(name = "trailerUrl")
    var trailerUrl: String? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)

