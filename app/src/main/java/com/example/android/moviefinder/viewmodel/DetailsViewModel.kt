package com.example.android.moviefinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviefinder.BuildConfig
import com.example.android.moviefinder.app.App
import com.example.android.moviefinder.model.MovieDetailsDTO
import com.example.android.moviefinder.model.local.*
import com.example.android.moviefinder.model.remote.RemoteDataSource
import com.example.android.moviefinder.model.remote.RemoteRepository
import com.example.android.moviefinder.model.remote.RemoteRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DetailsViewModel(
    private val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val favoritesLiveData: MutableLiveData<Boolean> = MutableLiveData(),
    private val remoteRepositoryImpl: RemoteRepository = RemoteRepositoryImpl(RemoteDataSource()),
    private val historyLocalRepositoryImpl: HistoryLocalRepository = HistoryLocalRepositoryImpl(App.getHistoryDao()),
    private val favoritesLocalRepositoryImpl: FavoritesLocalRepository = FavoritesLocalRepositoryImpl(
        App.getFavoritesDao()
    )
) : ViewModel() {
    val liveDataDetails: LiveData<AppState> = detailsLiveData
    val liveDataFavorites: LiveData<Boolean> = favoritesLiveData

    fun getMovieDetailsFromRemoteSource(id: Int) {
        detailsLiveData.value = AppState.Loading

        remoteRepositoryImpl.getMovieDetailsFromServer(
            id, Locale.getDefault().language, BuildConfig.TMDB_API_KEY,
            object : Callback<MovieDetailsDTO> {
                override fun onResponse(
                    call: Call<MovieDetailsDTO>,
                    response: Response<MovieDetailsDTO>
                ) {
                    val serverResponse: MovieDetailsDTO? = response.body()
                    detailsLiveData.postValue(
                        if (response.isSuccessful && serverResponse != null) {
                            checkResponse(serverResponse)
                        } else {
                            AppState.Error(Throwable("server error"))
                        }
                    )
                }

                override fun onFailure(call: Call<MovieDetailsDTO>, t: Throwable) {
                    detailsLiveData.postValue(AppState.Error(t))
                }

            }
        )
    }

    private fun checkResponse(response: MovieDetailsDTO): AppState {
        return if (response.isNotValid()) {
            AppState.Error(Throwable("corrupted data"))
        } else {
            AppState.Success(response)
        }
    }

    fun insertHistory(historyEntity: HistoryEntity) {
        Thread {
            historyLocalRepositoryImpl.insertHistory(historyEntity)
        }.start()
    }

    fun updateHistory(historyEntity: HistoryEntity) {
        Thread {
            historyLocalRepositoryImpl.updateHistory(historyEntity)
        }.start()
    }

    fun isFavorite(movieId: Int) {
        Thread {
            favoritesLiveData.postValue(
                favoritesLocalRepositoryImpl.getByMovieId(movieId).isNotEmpty()
            )
        }.start()
    }

    fun insertToFavorites(favoritesEntity: FavoritesEntity) {
        Thread {
            favoritesLocalRepositoryImpl.insertFavorites(favoritesEntity)
            favoritesLiveData.postValue(true)
        }.start()
    }

    fun deleteFromFavorites(movieId: Int) {
        Thread {
            favoritesLocalRepositoryImpl.deleteFavoritesByMovieId(movieId)
            favoritesLiveData.postValue(false)
        }.start()
    }
}