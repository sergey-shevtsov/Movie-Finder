package com.example.android.moviefinder.model.local

interface LocalRepository {
    fun getAllFavorites(): List<FavoritesEntity>
    fun addFavorite(favoritesEntity: FavoritesEntity)
    fun deleteFavorite(favoritesEntity: FavoritesEntity)
}