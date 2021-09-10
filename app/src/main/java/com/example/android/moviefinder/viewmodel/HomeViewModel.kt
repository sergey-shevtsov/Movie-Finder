package com.example.android.moviefinder.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviefinder.model.MovieListDTO
import com.example.android.moviefinder.model.MovieListLoader

class HomeViewModel : ViewModel() {
    private val liveDataToObserveNowPlaying: MutableLiveData<AppState> = MutableLiveData()
    private val liveDataToObservePopular: MutableLiveData<AppState> = MutableLiveData()
    private val liveDataToObserveTopRated: MutableLiveData<AppState> = MutableLiveData()
    private val movieListLoader = MovieListLoader(object : MovieListLoader.MovieListLoaderListener {
        override fun onLoaded(movieListDTO: MovieListDTO) {
            liveDataToObserveNowPlaying.postValue(AppState.Success(movieListDTO.results))
        }

        override fun onFailed(e: Throwable) {
            liveDataToObserveNowPlaying.postValue(AppState.Error(e))
        }

    })

    fun getLiveData(category: Category): LiveData<AppState> {
        return when (category) {
            Category.NOW_PLAYING -> liveDataToObserveNowPlaying
            Category.POPULAR -> liveDataToObservePopular
            Category.TOP_RATED -> liveDataToObserveTopRated
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getMovies(category: Category) {
        val requestName = when (category) {
            Category.NOW_PLAYING -> "now_playing"
            Category.POPULAR -> "popular"
            Category.TOP_RATED -> "top_rated"
        }
        getMoviesFromServer(requestName)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getMoviesFromServer(requestName: String) {
        liveDataToObserveNowPlaying.value = AppState.Loading
        movieListLoader.getMovies(requestName)
    }
}