package com.example.android.moviefinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviefinder.BuildConfig
import com.example.android.moviefinder.model.MovieDetailsDTO
import com.example.android.moviefinder.model.RemoteDataSource
import com.example.android.moviefinder.model.Repository
import com.example.android.moviefinder.model.RepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DetailsViewModel(
    private val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl(RemoteDataSource())
) : ViewModel() {
    val liveData: LiveData<AppState> = detailsLiveData

    fun getMovieDetailsFromRemoteSource(id: Int) {
        detailsLiveData.value = AppState.Loading

        repositoryImpl.getMovieDetailsFromServer(
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
}