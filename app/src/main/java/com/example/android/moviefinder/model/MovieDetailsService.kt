package com.example.android.moviefinder.model

import android.app.IntentService
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.android.moviefinder.BuildConfig
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val MOVIE_DETAILS_INTENT_FILTER = "MovieDetailsIntentFilter"
const val BASE_LOCALE = "en-US"
const val LOCALE_EXTRA = "Locale"
const val ID_EXTRA = "Id"
const val RESULT_EXTRA = "Result"
const val SUCCESS_RESULT = "SuccessResult"
const val ERROR_RESULT = "ErrorResult"
const val MOVIE_DETAILS_EXTRA = "MovieDetails"
const val EXCEPTION_EXTRA = "Exception"

class MovieDetailsService(name: String = "MovieDetailsService") : IntentService(name) {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val locale = intent.getStringExtra(LOCALE_EXTRA)
            val id = intent.getIntExtra(ID_EXTRA, -1)

            if (id == -1) {
                onEmptyData(Exception("data is empty"))
            } else {
                loadMovieDetails(locale, id)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadMovieDetails(locale: String? = BASE_LOCALE, id: Int) {
        val uri = try {
            URL("https://api.themoviedb.org/3/movie/$id?api_key=${BuildConfig.TMDB_API_KEY}&language=$locale")
        } catch (exception: MalformedURLException) {
            onMalformedURL(exception)
            return
        }

        var urlConnection: HttpsURLConnection? = null
        try {
            urlConnection = uri.openConnection() as HttpsURLConnection
            urlConnection.apply {
                requestMethod = "GET"
                readTimeout = 10000
            }

            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val result = reader.lines().collect(Collectors.joining("\n"))
            val movieDTO = Gson().fromJson(result, MovieDetailsDTO::class.java)

            onResponse(movieDTO)
        } catch (exception: Exception) {
            onErrorResponse(exception)
        } finally {
            urlConnection?.disconnect()
        }
    }

    private fun onResponse(movieDetailsDTO: MovieDetailsDTO?) {
        movieDetailsDTO?.let {
            onSuccessResponse(it)
        } ?: onEmptyResponse(Exception("empty response"))
    }

    private fun onSuccessResponse(movieDetailsDTO: MovieDetailsDTO) {
        sendResult(
            Intent(MOVIE_DETAILS_INTENT_FILTER)
                .putExtra(RESULT_EXTRA, SUCCESS_RESULT)
                .putExtra(MOVIE_DETAILS_EXTRA, movieDetailsDTO)
        )
    }

    private fun onEmptyResponse(exception: Exception) {
        sendException(exception)
    }

    private fun onErrorResponse(exception: Exception) {
        sendException(exception)
    }

    private fun onMalformedURL(exception: Exception) {
        sendException(exception)
    }

    private fun onEmptyData(exception: Exception) {
        sendException(exception)
    }

    private fun sendResult(intent: Intent) {
        LocalBroadcastManager.getInstance(this)
            .sendBroadcast(intent)
    }

    private fun sendException(exception: Exception) {
        sendResult(
            Intent(MOVIE_DETAILS_INTENT_FILTER)
                .putExtra(RESULT_EXTRA, ERROR_RESULT)
                .putExtra(EXCEPTION_EXTRA, exception)
        )
    }
}