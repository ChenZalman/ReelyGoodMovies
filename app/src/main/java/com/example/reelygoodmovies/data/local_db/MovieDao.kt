package com.example.reelygoodmovies.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.reelygoodmovies.data.models.Movie
import com.example.reelygoodmovies.data.models.TrailerResponse

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovies(movies: List<Movie>)

    @Delete
    suspend fun deleteMovie(vararg movies: Movie)

    @Update
    suspend fun updateMovie(movie: Movie)

    @Query("SELECT * FROM movies")
    fun getMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovie(id: Int): LiveData<Movie>

    @Query("SELECT trailerUrl FROM movies WHERE id = :id")
    fun getMovieTrailer(id: Int): LiveData<String?>

    @Query("UPDATE movies SET trailerUrl = :trailerUrl WHERE id = :id")
    suspend fun updateMovieTrailer(id: Int, trailerUrl: String)

    @Query("SELECT * FROM movies WHERE favorite = 1")
    fun getFavoriteMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE favorite = 1")
    suspend fun getFavoriteMoviesSync(): List<Movie>

    @Query("SELECT * FROM movies WHERE id = :id AND favorite = 1")
    suspend fun getFavoriteMovieById(id: Int): Movie?
}
