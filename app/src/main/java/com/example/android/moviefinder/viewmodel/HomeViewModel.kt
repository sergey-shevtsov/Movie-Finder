package com.example.android.moviefinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviefinder.BuildConfig
import com.example.android.moviefinder.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeViewModel(
    private val repositoryImpl: Repository = RepositoryImpl(RemoteDataSource()),
    private val homeLiveDataNowPlaying: MutableLiveData<AppState> = MutableLiveData(),
    private val homeLiveDataPopular: MutableLiveData<AppState> = MutableLiveData(),
    private val homeLiveDataTopRated: MutableLiveData<AppState> = MutableLiveData()
) : ViewModel() {
    val liveDataNowPlaying: LiveData<AppState> = homeLiveDataNowPlaying
    val liveDataPopular: LiveData<AppState> = homeLiveDataPopular
    val liveDataTopRated: LiveData<AppState> = homeLiveDataTopRated

    fun getNowPlayingMovieListFromRemoteSource(page: Int = 1) {
        getMovieListFromRemoteSource(homeLiveDataNowPlaying,"now_playing", page)
    }

    fun getPopularMovieListFromRemoteSource(page: Int = 1) {
        getMovieListFromRemoteSource(homeLiveDataPopular,"popular", page)
    }

    fun getTopRatedMovieListFromRemoteSource(page: Int = 1) {
        getMovieListFromRemoteSource(homeLiveDataTopRated,"top_rated", page)
    }

    private fun getMovieListFromRemoteSource(liveData: MutableLiveData<AppState>, category: String, page: Int) {
        liveData.value = AppState.Loading

        repositoryImpl.getMovieListFromServer(
            category, Locale.getDefault().language, page, BuildConfig.TMDB_API_KEY,
            object : Callback<MovieListDTO> {
                override fun onResponse(
                    call: Call<MovieListDTO>,
                    response: Response<MovieListDTO>
                ) {
                    val serverResponse: MovieListDTO? = response.body()
                    liveData.postValue(
                        if (response.isSuccessful && serverResponse != null) {
                            checkResponse(serverResponse)
                        } else {
                            AppState.Error(Throwable("server error"))
                        }
                    )
                }

                override fun onFailure(call: Call<MovieListDTO>, t: Throwable) {
                    liveData.postValue(AppState.Error(t))
                }

            }
        )
    }

    private fun checkResponse(response: MovieListDTO): AppState {
        return if (response.results.isNullOrEmpty()) {
            AppState.Error(Throwable("corrupted data"))
        } else {
            AppState.Success(response)
        }
    }
}