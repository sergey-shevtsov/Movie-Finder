package com.example.android.moviefinder.view.detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.DetailFragmentBinding
import com.example.android.moviefinder.model.GenresDTO
import com.example.android.moviefinder.model.MovieApiLoader
import com.example.android.moviefinder.model.MovieDTO
import com.example.android.moviefinder.view.*
import com.example.android.moviefinder.viewmodel.DetailViewModel
import java.util.*

class DetailFragment : Fragment() {

    companion object {
        const val MOVIE_ID_KEY = "MOVIE_EXTRA"
        fun newInstance(bundle: Bundle): DetailFragment {
            val fragment = DetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this).get(DetailViewModel::class.java)
    }
    private val movieLoader: MovieApiLoader.MovieLoader by lazy {
        MovieApiLoader.MovieLoader(object : MovieApiLoader.MovieLoader.MovieLoaderListener {
            override fun onLoading() {
                binding.apply {
                    errorFrame.errorContainer.hide()
                    loadingFrame.loadingContainer.show()
                }
            }

            override fun onLoaded(movieDTO: MovieDTO) {
                binding.apply {
                    errorFrame.errorContainer.hide()
                    loadingFrame.loadingContainer.hide()
                    fillDetail(movieDTO)
                }
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onFailed(throwable: Throwable) {
                binding.apply {
                    loadingFrame.loadingContainer.hide()
                    errorFrame.errorContainer.show()
                    errorFrame.errorMessage.text =
                        "${resources.getString(R.string.error)}: ${throwable.message}"
                    errorFrame.errorActionButton.setOnClickListener {
                        getMovie()
                    }
                }
            }

        })
    }
    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!
    private var movieId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)

        activity?.showHomeButton()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.getInt(MOVIE_ID_KEY)?.let {
            this.movieId = it
        }

        getMovie()

        binding.favoritesButton.setOnClickListener {
            Toast.makeText(context, "To favorites!", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getMovie() {
        movieLoader.getMovieById("ru-RU", movieId)
    }

    private fun fillDetail(movie: MovieDTO) {
        val releaseYear = movie.release_date?.subSequence(0, 4).toString()
        binding.apply {
            errorFrame.errorContainer.hide()
            loadingFrame.loadingContainer.hide()
            title.text = movie.title
            originalTitle.text = "${movie.original_title} ($releaseYear)"
            image.setImageResource(R.drawable.dummy)
            genres.text = movie.genres?.let { getGenresNames(it) }
            duration.text = "${movie.runtime} ${resources.getString(R.string.minute)}/ ${
                movie.runtime?.let { it.getFormatDuration() }
            }"
            rating.text = "${movie.vote_average} (${movie.vote_count})"
            budget.text = "${resources.getString(R.string.budget)} \$${movie.budget}"
            revenue.text = "${resources.getString(R.string.revenue)} \$${movie.revenue}"
            released.text =
                "${resources.getString(R.string.released)} ${movie.release_date?.let { it.formatDate() }}"
            overview.text = movie.overview
        }
    }

    private fun getGenresNames(genres: Array<GenresDTO.GenreDTO>): String {
        val sb = StringBuilder()
        for (i in genres.indices) {
            sb.append(genres[i].name)
            if (i != genres.lastIndex) sb.append(", ")
        }
        return sb.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}