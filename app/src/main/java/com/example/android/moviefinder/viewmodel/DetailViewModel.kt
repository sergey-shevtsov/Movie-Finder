package com.example.android.moviefinder.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviefinder.model.MovieDTO
import com.example.android.moviefinder.model.MovieLoader
import com.example.android.moviefinder.model.RepositoryImpl

class DetailViewModel : ViewModel() {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val movieLoader = MovieLoader(movieListener = object : MovieLoader.MovieLoaderListener {
        override fun onLoaded(movieDTO: MovieDTO) {
            liveDataToObserve.postValue(AppState.Success(movieDTO))
        }

        override fun onFailed(throwable: Throwable) {
            liveDataToObserve.postValue(AppState.Error(throwable))
        }
    })

    fun getLiveData(): LiveData<AppState> {
        return liveDataToObserve
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getMovieById(id: Int) {
        getMovieByIdFromServer(id)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getMovieByIdFromServer(id: Int) {
        liveDataToObserve.value = AppState.Loading
        movieLoader.getMovieById(id)
    }
}