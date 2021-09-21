package com.example.android.moviefinder.model.remote

import com.example.android.moviefinder.model.MovieDetailsDTO
import com.example.android.moviefinder.model.MovieListDTO
import retrofit2.Callback

class RemoteRepositoryImpl(private val remoteDataSource: RemoteDataSource) : RemoteRepository {
    override fun getMovieDetailsFromServer(
        id: Int,
        language: String,
        apiKey: String,
        callback: Callback<MovieDetailsDTO>
    ) {
        remoteDataSource.getMovieDetails(id, language, apiKey, callback)
    }

    override fun getMovieListFromServer(
        category: String,
        language: String,
        page: Int,
        apiKey: String,
        callback: Callback<MovieListDTO>
    ) {
        remoteDataSource.getMovieList(category, language, page, apiKey, callback)
    }
}