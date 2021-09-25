package com.example.android.moviefinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviefinder.app.App
import com.example.android.moviefinder.model.local.HistoryEntity
import com.example.android.moviefinder.model.local.HistoryLocalRepository
import com.example.android.moviefinder.model.local.HistoryLocalRepositoryImpl

class HistoryViewModel(
    private val historyLocalRepository: HistoryLocalRepository = HistoryLocalRepositoryImpl(App.getHistoryDao()),
    private val historyLiveData: MutableLiveData<AppState> = MutableLiveData()
) : ViewModel() {
    val liveData: LiveData<AppState> = historyLiveData

    fun getAllHistory() {
        historyLiveData.value = AppState.Loading

        Thread {

            historyLiveData.postValue(AppState.Success(historyLocalRepository.getAllHistory()))

        }.start()
    }

    fun deleteHistory(historyEntity: HistoryEntity) {
        Thread {

            historyLocalRepository.deleteHistory(historyEntity)

        }.start()

        getAllHistory()
    }

    fun deleteAllHistory() {
        Thread {

            historyLocalRepository.clearHistory()

        }.start()

        getAllHistory()
    }
}