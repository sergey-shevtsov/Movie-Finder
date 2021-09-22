package com.example.android.moviefinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviefinder.app.App
import com.example.android.moviefinder.model.local.HistoryEntity
import com.example.android.moviefinder.model.local.LocalRepository
import com.example.android.moviefinder.model.local.LocalRepositoryImpl

class HistoryViewModel(
    private val localRepository: LocalRepository = LocalRepositoryImpl(App.getHistoryDao()),
    private val historyLiveData: MutableLiveData<AppState> = MutableLiveData()
) : ViewModel() {
    val liveData: LiveData<AppState> = historyLiveData

    fun getAllHistory() {
        historyLiveData.value = AppState.Loading

        Thread {

            historyLiveData.postValue(AppState.Success(localRepository.getAllHistory()))

        }.start()
    }

    fun deleteHistory(historyEntity: HistoryEntity) {
        Thread {

            localRepository.deleteHistory(historyEntity)

        }.start()

        getAllHistory()
    }

    fun deleteAllHistory() {
        Thread {

            localRepository.clearHistory()

        }.start()

        getAllHistory()
    }
}