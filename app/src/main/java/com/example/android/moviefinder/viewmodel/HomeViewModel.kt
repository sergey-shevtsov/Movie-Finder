package com.example.android.moviefinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviefinder.model.RepositoryImpl

class HomeViewModel : ViewModel() {
    private val repository = RepositoryImpl()
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    fun getData(): LiveData<AppState> {
        getMovies()
        return liveDataToObserve
    }

    fun getMovies() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading

        Thread {
            Thread.sleep(500)
            liveDataToObserve.postValue(AppState.Success(repository.getMoviesFromLocalStorage()))
        }.start()
    }
}