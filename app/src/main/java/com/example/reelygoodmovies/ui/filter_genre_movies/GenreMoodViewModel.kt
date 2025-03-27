package com.example.reelygoodmovies.ui.filter_genre_movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reelygoodmovies.R
import com.example.reelygoodmovies.data.models.Movie

class GenreMoodViewModel : ViewModel() {

    private val _comedyMovies = MutableLiveData<List<Movie>>()
    val comedyMovies: LiveData<List<Movie>> = _comedyMovies

    private val _thrillerMovies = MutableLiveData<List<Movie>>()
    val thrillerMovies: LiveData<List<Movie>> = _thrillerMovies

    private val _fantasyMovies = MutableLiveData<List<Movie>>()
    val fantasyMovies: LiveData<List<Movie>> = _fantasyMovies

    private val _romanceMovies = MutableLiveData<List<Movie>>()
    val romanceMovies: LiveData<List<Movie>> = _romanceMovies

    private val _familyMovies = MutableLiveData<List<Movie>>()
    val familyMovies: LiveData<List<Movie>> = _familyMovies

    fun filterMoviesByGenre(movies: List<Movie>) {
        _comedyMovies.value = movies.filter { it.genre.contains(R.string.comedy_label) }
        _thrillerMovies.value = movies.filter { it.genre.contains(R.string.thriller_label) }
        _fantasyMovies.value = movies.filter { it.genre.contains(R.string.science_fiction_label) }
        _romanceMovies.value = movies.filter { it.genre.contains(R.string.romance_label) }
        _familyMovies.value = movies.filter { it.genre.contains(R.string.family_label) }
    }
}