package com.example.android.moviefinder.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.DetailFragmentBinding
import com.example.android.moviefinder.model.Movie
import com.example.android.moviefinder.viewmodel.AppState
import com.example.android.moviefinder.viewmodel.DetailViewModel
import java.util.*

class DetailFragment : Fragment() {

    companion object {
        const val MOVIE_KEY = "MOVIE_EXTRA"
        fun newInstance(bundle: Bundle): DetailFragment {
            val fragment = DetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var viewModel: DetailViewModel
    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)

        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.getData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        val movieId = arguments?.getInt(MOVIE_KEY)
        if (movieId != null) {
            viewModel.getMovieById(movieId)
        }


        binding.favoritesButton.setOnClickListener {
            Toast.makeText(context, "To favorites!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Loading -> {

            }
            is AppState.Success -> {
                val movie = state.data as Movie
                val releaseYear = movie.released.subSequence(6, 10).toString()
                binding.apply {
                    title.text = movie.title
                    originalTitle.text = "${movie.originalTitle} ($releaseYear)"
                    image.setImageResource(movie.imageId)
                    genres.text = movie.genres.joinToString(", ")
                    duration.text = "${movie.duration} ${resources.getString(R.string.minute)}/ ${getFormatDuration(movie.duration)}"
                    rating.text = "${movie.rating} (${movie.voteCount})"
                    budget.text = "${resources.getString(R.string.budget)} $${movie.budget}"
                    revenue.text = "${resources.getString(R.string.revenue)} $${movie.revenue}"
                    released.text = "${resources.getString(R.string.released)} ${movie.released}"
                    overview.text = movie.overview
                }
            }
            is AppState.Error -> {

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