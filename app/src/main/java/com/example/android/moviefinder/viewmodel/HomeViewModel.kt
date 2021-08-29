package com.example.android.moviefinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviefinder.model.Movie
import com.example.android.moviefinder.model.RepositoryImpl
import java.lang.Exception
import kotlin.random.Random

class HomeViewModel : ViewModel() {
    private val repository = RepositoryImpl()
    private val firstCategoryLiveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    val firstCategoryLiveData: LiveData<AppState> = firstCategoryLiveDataToObserve

    fun getFirstSectionMovies() = getFirstCategoryDataFromLocalSource()

    private fun getFirstCategoryDataFromLocalSource() {
        firstCategoryLiveDataToObserve.value = AppState.Loading

        Thread {
            Thread.sleep(2000)
            if (Random.nextBoolean()) {
                firstCategoryLiveDataToObserve.postValue(AppState.Success(repository.getMoviesFromServer()))
            } else {
                firstCategoryLiveDataToObserve.postValue(AppState.Error(Exception("no internet")))
            }
        }.start()
    }
}