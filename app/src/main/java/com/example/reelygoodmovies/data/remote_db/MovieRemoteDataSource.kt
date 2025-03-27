package com.example.reelygoodmovies.data.remote_db

import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    private val movieService: MovieService
) : BaseDataSource() {
    suspend fun getMovies() = getResult { movieService.getAllMovies() }
    suspend fun getMovie(id : Int) = getResult {movieService.getMovie(id)}
    suspend fun getMovieTrailer(id: Int) = getResult { movieService.getMovieTrailer(id) }

}