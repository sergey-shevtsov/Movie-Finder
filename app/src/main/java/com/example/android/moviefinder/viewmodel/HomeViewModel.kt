package com.example.android.moviefinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviefinder.model.RepositoryImpl

class HomeViewModel : ViewModel() {
    private val repository = RepositoryImpl()
    private val liveDataToObserveRecommended: MutableLiveData<AppState> = MutableLiveData()
    private val liveDataToObserveTopRated: MutableLiveData<AppState> = MutableLiveData()
    private val liveDataToObserveComedy: MutableLiveData<AppState> = MutableLiveData()

    fun getLiveData(category: Category): LiveData<AppState> {
        return when (category) {
            Category.RECOMMENDED -> liveDataToObserveRecommended
            Category.TOP_RATED -> liveDataToObserveTopRated
            Category.COMEDY -> liveDataToObserveComedy
        }
    }

    fun getMovies(category: Category) {
        getMoviesFromLocalSource(category)
    }

    private fun getMoviesFromLocalSource(category: Category) {
        when (category) {

            Category.RECOMMENDED -> {
                liveDataToObserveRecommended.value = AppState.Loading

                Thread {
                    Thread.sleep(800)
                    liveDataToObserveRecommended.postValue(AppState.Success(repository.getRecommendedMoviesFromLocalStorage()))
                }.start()
            }

            Category.TOP_RATED -> {
                liveDataToObserveTopRated.value = AppState.Loading

                Thread {
                    Thread.sleep(700)
                    liveDataToObserveTopRated.postValue(AppState.Success(repository.getTopRatedMoviesFromLocalStorage()))
                }.start()
            }

            Category.COMEDY -> {
                liveDataToObserveComedy.value = AppState.Loading

                Thread {
                    Thread.sleep(500)
                    liveDataToObserveComedy.postValue(AppState.Success(repository.getComedyMoviesFromLocalStorage()))
                }.start()
            }

        }

    }
}