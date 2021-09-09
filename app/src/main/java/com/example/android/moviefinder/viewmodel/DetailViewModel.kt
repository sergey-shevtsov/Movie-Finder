package com.example.android.moviefinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviefinder.model.RepositoryImpl

class DetailViewModel : ViewModel() {
    private val repository = RepositoryImpl()
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    fun getData(): LiveData<AppState> {
        return liveDataToObserve
    }

    fun getMovieById(id: Int) {
        getMovieByIdFromLocalSource(id)
    }

    private fun getMovieByIdFromLocalSource(id: Int) {
        liveDataToObserve.value = AppState.Loading

        Thread {
            Thread.sleep(500)
            val movie = repository.getMovieByIdFromLocalStorage(id)
            if (movie != null) {
                liveDataToObserve.postValue(AppState.Success(movie))
            } else {
                liveDataToObserve.postValue(AppState.Error(Exception("movie not found")))
            }
        }.start()
    }
}