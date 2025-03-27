package com.example.reelygoodmovies.data.models

import com.google.gson.annotations.SerializedName

data class AllMovies(
    val page: Int,

    @SerializedName("total_results")
    val totalResults: Int,

    @SerializedName("total_pages")
    val totalPages: Int,

    val results: List<Movie>
)
