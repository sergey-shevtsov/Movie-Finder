package com.example.android.moviefinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviefinder.app.App
import com.example.android.moviefinder.model.local.FavoritesLocalRepository
import com.example.android.moviefinder.model.local.FavoritesLocalRepositoryImpl

class FavoritesViewModel(
    private val favoritesLocalRepository: FavoritesLocalRepository = FavoritesLocalRepositoryImpl(
        App.getFavoritesDao()
    ),
    private val favoritesLiveData: MutableLiveData<AppState> = MutableLiveData()
) : ViewModel() {
    val liveData: LiveData<AppState> = favoritesLiveData

    fun getAllFavorites() {
        favoritesLiveData.value = AppState.Loading

        Thread {
            favoritesLiveData.postValue(AppState.Success(favoritesLocalRepository.getAllFavorites()))
        }.start()
    }
}