package com.example.android.moviefinder.viewmodel

import android.os.Handler
import android.os.HandlerThread
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
    private val handlerThread = HandlerThread("handlerThread").also { it.start() }
    private val handler = Handler(handlerThread.looper)

    fun getAllHistory() {
        historyLiveData.value = AppState.Loading

        handler.post {
            historyLiveData.postValue(AppState.Success(historyLocalRepository.getAllHistory()))
        }
    }

    fun deleteHistory(historyEntity: HistoryEntity) {
        handler.post {
            historyLocalRepository.deleteHistory(historyEntity)
        }

        getAllHistory()
    }

    fun deleteAllHistory() {
        handler.post {
            historyLocalRepository.clearHistory()
            historyLiveData.postValue(AppState.Success(emptyList<HistoryEntity>()))
        }
    }

    override fun onCleared() {
        handlerThread.quitSafely()
        super.onCleared()
    }
}