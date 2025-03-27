package com.example.reelygoodmovies.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.reelygoodmovies.data.models.Movie
import com.example.reelygoodmovies.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    application: Application, private val moviesRepository : MovieRepository
) : AndroidViewModel(application) {

    val movies = moviesRepository.getMovies()

    val favoriteMovies: LiveData<List<Movie>> = moviesRepository.getFavoriteMovies()

    private val _chosenMovie = MutableLiveData<Movie>()
    val chosenMovie: LiveData<Movie> get() = _chosenMovie

    private val _favorite = MutableLiveData<Boolean>()
    val favorite: LiveData<Boolean> get() = _favorite

    private val _filteredMovies = MutableLiveData<List<Movie>>()
    val filteredMovies: LiveData<List<Movie>> get() = _filteredMovies

    private val _recognition = MutableLiveData<String>()
    val recognition : LiveData<String> get() = _recognition

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> get() = _searchQuery

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun initializeSearch() {
        setRecognition("")
        setFilteredMovies(movies.value?.status?.data ?: emptyList())
        setSearchQuery("")
    }

    fun filterMovies(query: String) {
        val filteredMovies = movies.value?.status?.data?.filter {
            it.title.lowercase().contains(query.lowercase())
        } ?: emptyList()
        setFilteredMovies(filteredMovies)
    }

    fun setRecognition(text: String) {
        _recognition.value = text
    }

    private fun setFilteredMovies(movies: List<Movie>) {
        _filteredMovies.value = movies
    }

    fun setMovie(movie: Movie) {
        _chosenMovie.value = movie
    }

    fun addMovie(movie: Movie) {
        viewModelScope.launch { moviesRepository.addMovie(movie) }
    }

    fun deleteMovie(movie: Movie, query: String?) {
        viewModelScope.launch {
            moviesRepository.deleteMovie(movie)

            val updatedMovies = movies.value?.status?.data?.filter { it.id != movie.id } ?: emptyList()
            val filteredMovies = if (!query.isNullOrEmpty()) {
                updatedMovies.filter { it.title.lowercase().contains(query.lowercase()) }
            } else {
                updatedMovies
            }
            setFilteredMovies(filteredMovies)
        }
    }

    fun updateMovie(movie: Movie) {
        viewModelScope.launch { moviesRepository.updateMovie(movie) }
    }

}