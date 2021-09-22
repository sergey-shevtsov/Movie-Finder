package com.example.android.moviefinder.model.local

class LocalRepositoryImpl(private val historyDao: HistoryDao) : LocalRepository {
    override fun getAllHistory(): List<HistoryEntity> {
        return historyDao.all()
    }

    override fun getAllHistoryByMovieId(movieId: Int): List<HistoryEntity> {
        return historyDao.getHistoryByMovieId(movieId)
    }

    override fun insertHistory(historyEntity: HistoryEntity) {
        historyDao.insert(historyEntity)
    }

    override fun updateHistory(historyEntity: HistoryEntity) {
        historyDao.update(historyEntity)
    }

    override fun deleteHistory(historyEntity: HistoryEntity) {
        historyDao.delete(historyEntity)
    }

    override fun clearHistory() {
        historyDao.clear()
    }
}