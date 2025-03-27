package com.example.reelygoodmovies.data.remote_db

import com.example.reelygoodmovies.data.models.AllMovies
import com.example.reelygoodmovies.data.models.Movie
import com.example.reelygoodmovies.data.models.TrailerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {
    @GET("movie/popular")
    suspend fun getAllMovies() : Response<AllMovies>

    @GET("movie/{id}")
    suspend fun getMovie(@Path("id")id :Int) : Response<Movie>

    @GET("movie/{id}/videos")
    suspend fun getMovieTrailer(@Path("id") id: Int): Response<TrailerResponse>
}