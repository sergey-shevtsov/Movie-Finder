package com.example.android.moviefinder.model.local

interface LocalRepository {

    fun getAllHistory(): List<HistoryEntity>

    fun getAllHistoryByMovieId(movieId: Int): List<HistoryEntity>

    fun insertHistory(historyEntity: HistoryEntity)

    fun updateHistory(historyEntity: HistoryEntity)

    fun deleteHistory(historyEntity: HistoryEntity)

    fun clearHistory()

}