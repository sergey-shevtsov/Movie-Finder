package com.example.android.moviefinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviefinder.model.Movie
import java.lang.Exception
import kotlin.random.Random

class HomeViewModel : ViewModel() {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    val liveData: LiveData<AppState> = liveDataToObserve

    fun getFirstSectionMovies() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading

        Thread {
            Thread.sleep(3000)
            if (Random.nextBoolean()) {
                liveDataToObserve.postValue(AppState.Success(arrayListOf(
                   Movie(),
                   Movie(),
                   Movie(),
                   Movie(),
                   Movie(),
                   Movie(),
                   Movie()
                )))
            } else {
                liveDataToObserve.postValue(AppState.Error(Exception("ошибка сети")))
            }
        }.start()
    }
}