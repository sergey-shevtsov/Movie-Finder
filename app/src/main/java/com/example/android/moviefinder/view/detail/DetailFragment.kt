package com.example.android.moviefinder.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.DetailFragmentBinding
import com.example.android.moviefinder.model.Movie
import com.example.android.moviefinder.view.hide
import com.example.android.moviefinder.view.show
import com.example.android.moviefinder.view.showHome
import com.example.android.moviefinder.view.showSnackBar
import com.example.android.moviefinder.viewmodel.AppState
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
    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!
    private var movieId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)

        activity?.showHome()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        val movieId = arguments?.getInt(MOVIE_ID_KEY)
        if (movieId != null) {
            this.movieId = movieId
            viewModel.getMovieById(movieId)
        }


        binding.favoritesButton.setOnClickListener {
            Toast.makeText(context, "To favorites!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Loading -> {
                binding.apply {
                    errorFrame.visibility = View.GONE
                    binding.loadingFrame.visibility = View.VISIBLE
                }
            }
            is AppState.Success -> {
                val movie = state.data as Movie
                val releaseYear = movie.released.subSequence(6, 10).toString()
                binding.apply {
                    errorFrame.hide()
                    loadingFrame.hide()
                    title.text = movie.title
                    originalTitle.text = "${movie.originalTitle} ($releaseYear)"
                    image.setImageResource(movie.imageId)
                    genres.text = movie.genres.joinToString(", ")
                    duration.text = "${movie.duration} ${resources.getString(R.string.minute)}/ ${getFormatDuration(movie.duration)}"
                    rating.text = "${movie.rating} (${movie.voteCount})"
                    budget.text = "${resources.getString(R.string.budget)} ${movie.budget}"
                    revenue.text = "${resources.getString(R.string.revenue)} ${movie.revenue}"
                    released.text = "${resources.getString(R.string.released)} ${movie.released}"
                    overview.text = movie.overview
                }
            }
            is AppState.Error -> {
                binding.apply {
                    loadingFrame.hide()
                    errorFrame.show()
                    errorFrame.showSnackBar(
                        "${resources.getString(R.string.error)}: ${state.error.message}",
                        resources.getString(R.string.reload),
                        { viewModel.getMovieById(movieId) }
                    )
                }
            }
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