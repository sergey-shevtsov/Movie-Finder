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
import com.example.android.moviefinder.model.Movie
import com.example.android.moviefinder.model.MovieDTO
import com.example.android.moviefinder.view.hide
import com.example.android.moviefinder.view.show
import com.example.android.moviefinder.view.showHomeButton
import com.example.android.moviefinder.viewmodel.AppState
import com.example.android.moviefinder.viewmodel.DetailViewModel
import com.example.android.moviefinder.viewmodel.MovieNotFoundException
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

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        val movieId = arguments?.getInt(MOVIE_ID_KEY)
        movieId?.let {
            this.movieId = it
            viewModel.getMovieById(movieId)
        }


        binding.favoritesButton.setOnClickListener {
            Toast.makeText(context, "To favorites!", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun renderData(state: AppState) {
        when (state) {

            is AppState.Loading -> {
                binding.apply {
                    errorFrame.hide()
                    loadingFrame.show()
                }
            }

            is AppState.Success -> {
                val movie = state.data as MovieDTO
                fillDetail(movie)
            }

            is AppState.Error -> {
                binding.apply {
                    loadingFrame.hide()
                    errorFrame.show()
                    val e = state.error as MovieNotFoundException
                    errorMessage.text = "${resources.getString(R.string.error)}: ${e.message}"
                    errorActionButton.setOnClickListener {
                        viewModel.getMovieById(e.id)
                    }
                }
            }

        }
    }

    private fun fillDetail(movie: MovieDTO) {
        val releaseYear = movie.release_date?.subSequence(0, 4).toString()
        binding.apply {
            errorFrame.hide()
            loadingFrame.hide()
            title.text = movie.title
            originalTitle.text = "${movie.original_title} ($releaseYear)"
            image.setImageResource(R.drawable.dummy)
//            genres.text = movie.genres.joinToString(", ")
            duration.text = "${movie.runtime} ${resources.getString(R.string.minute)}/ ${
                movie.runtime?.let { getFormatDuration(it) }
            }"
            rating.text = "${movie.vote_average} (${movie.vote_count})"
            budget.text = "${resources.getString(R.string.budget)} ${movie.budget}"
            revenue.text = "${resources.getString(R.string.revenue)} ${movie.revenue}"
            released.text = "${resources.getString(R.string.released)} ${movie.release_date}"
            overview.text = movie.overview
        }
    }

    private fun getFormatDuration(duration: Int): String {
        val hours = formatNum(duration / 60)
        val minutes = formatNum(duration % 60)
        return String.format(Locale.getDefault(), "%s:%s", hours, minutes)
    }

    private fun formatNum(num: Int): String {
        if (num < 10) return "0$num"
        return num.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}