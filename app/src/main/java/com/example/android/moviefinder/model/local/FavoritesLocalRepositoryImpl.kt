package com.example.android.moviefinder.model.local

class FavoritesLocalRepositoryImpl(private val favoritesDao: FavoritesDao) :
    FavoritesLocalRepository {

    override fun getAllFavorites(): List<FavoritesEntity> {
        return favoritesDao.all()
    }

    override fun getByMovieId(movieId: Int): List<FavoritesEntity> {
        return favoritesDao.getByMovieId(movieId)
    }

    override fun insertFavorites(favoritesEntity: FavoritesEntity) {
        favoritesDao.insert(favoritesEntity)
    }

    override fun deleteFavoritesByMovieId(movieId: Int) {
        favoritesDao.deleteByMovieId(movieId)
    }

    override fun clearFavorites() {
        favoritesDao.clear()
    }

}