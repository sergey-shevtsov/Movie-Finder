package com.example.android.moviefinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviefinder.model.RepositoryImpl

class HomeViewModel : ViewModel() {
    private val repository = RepositoryImpl()
    private val liveDataToObserveRecommendedMovies: MutableLiveData<AppState> = MutableLiveData()
    private val liveDataToObserveTopRatedMovies: MutableLiveData<AppState> = MutableLiveData()
    private val liveDataToObserveComedyMovies: MutableLiveData<AppState> = MutableLiveData()

    fun getRecommendedMoviesLiveData(): LiveData<AppState> {
        getRecommendedMovies()
        return liveDataToObserveRecommendedMovies
    }

    fun getTopRatedMoviesLiveData(): LiveData<AppState> {
        getTopRatedMovies()
        return liveDataToObserveTopRatedMovies
    }

    fun getComedyMoviesLiveData(): LiveData<AppState> {
        getComedyMovies()
        return liveDataToObserveComedyMovies
    }

    fun getRecommendedMovies() = getRecommendedMoviesFromLocalSource()
    fun getTopRatedMovies() = getTopRatedMoviesFromLocalSource()
    fun getComedyMovies() = getComedyMoviesFromLocalSource()

    private fun getRecommendedMoviesFromLocalSource() {
        liveDataToObserveRecommendedMovies.value = AppState.Loading

        Thread {
            Thread.sleep(500)
            liveDataToObserveRecommendedMovies.postValue(AppState.Success(repository.getRecommendedMoviesFromLocalStorage()))
        }.start()
    }

    private fun getTopRatedMoviesFromLocalSource() {
        liveDataToObserveTopRatedMovies.value = AppState.Loading

        Thread {
            Thread.sleep(500)
            liveDataToObserveTopRatedMovies.postValue(AppState.Success(repository.getTopRatedMoviesFromLocalStorage()))
        }.start()
    }

    private fun getComedyMoviesFromLocalSource() {
        liveDataToObserveComedyMovies.value = AppState.Loading

        Thread {
            Thread.sleep(500)
            liveDataToObserveComedyMovies.postValue(AppState.Success(repository.getComedyMoviesFromLocalStorage()))
        }.start()
    }
}