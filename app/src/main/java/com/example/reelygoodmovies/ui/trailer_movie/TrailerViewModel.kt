    package com.example.reelygoodmovies.ui.trailer_movie

    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.switchMap
    import com.example.reelygoodmovies.data.repositories.MovieRepository
    import com.example.reelygoodmovies.utils.Resource
    import dagger.hilt.android.lifecycle.HiltViewModel
    import javax.inject.Inject

    @HiltViewModel
    class TrailerViewModel @Inject constructor(
        private val movieRepository: MovieRepository
    ) : ViewModel() {

        private val _id = MutableLiveData<Int>()
        private val _trailer = _id.switchMap { movieId ->
            movieRepository.getMovieTrailer(movieId)
        }
        val trailer: LiveData<Resource<String?>> = _trailer

        fun setId(id: Int) {
            _id.value = id
        }

    }







