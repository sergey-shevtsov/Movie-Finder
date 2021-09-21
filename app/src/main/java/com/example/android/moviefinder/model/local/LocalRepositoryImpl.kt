package com.example.android.moviefinder.model.local

class LocalRepositoryImpl(private val localDataSource: FavoritesDao) : LocalRepository {
    override fun getAllFavorites(): List<FavoritesEntity> {
        return localDataSource.all()
    }

    override fun addFavorite(favoritesEntity: FavoritesEntity) {
        localDataSource.insert(favoritesEntity)
    }

    override fun deleteFavorite(favoritesEntity: FavoritesEntity) {
        localDataSource.delete(favoritesEntity)
    }
}