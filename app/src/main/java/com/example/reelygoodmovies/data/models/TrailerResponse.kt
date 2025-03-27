package com.example.reelygoodmovies.data.models

data class TrailerResponse(
    val results: List<Trailer> = emptyList()
) {
    data class Trailer(
        val key: String?,
        val type: String
    )
}


