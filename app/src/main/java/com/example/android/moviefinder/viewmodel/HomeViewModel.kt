package com.example.android.moviefinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviefinder.model.RepositoryImpl
import java.lang.Exception
import kotlin.random.Random

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
            Thread.sleep(2000)
//            if (Random.nextBoolean()) {
//                liveDataToObserve.postValue(AppState.Success(repository.getMoviesFromLocalStorage()))
//            } else {
//                liveDataToObserve.postValue(AppState.Error(Exception("no internet")))
//            }
            liveDataToObserve.postValue(AppState.Success(repository.getMoviesFromLocalStorage()))
        }.start()
    }
}