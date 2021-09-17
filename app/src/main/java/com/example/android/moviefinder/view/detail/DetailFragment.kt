package com.example.android.moviefinder.view.detail

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.DetailFragmentBinding
import com.example.android.moviefinder.model.*
import com.example.android.moviefinder.view.*
import com.example.android.moviefinder.viewmodel.DetailViewModel

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
    private val movieId: Int by lazy {
        arguments?.getInt(MOVIE_ID_KEY) ?: -1
    }
//    private val movieLoader: MovieApiLoader.MovieLoader by lazy {
//        MovieApiLoader.MovieLoader(object : MovieApiLoader.MovieLoader.MovieLoaderListener {
//            override fun onLoading() {
//                binding.apply {
//                    errorFrame.errorContainer.hide()
//                    loadingFrame.loadingContainer.show()
//                }
//            }
//
//            override fun onLoaded(movieDTO: MovieDTO) {
//                binding.apply {
//                    errorFrame.errorContainer.hide()
//                    loadingFrame.loadingContainer.hide()
//                    fillDetail(movieDTO)
//                }
//            }
//
//            @RequiresApi(Build.VERSION_CODES.N)
//            override fun onFailed(throwable: Throwable) {
//                binding.apply {
//                    loadingFrame.loadingContainer.hide()
//                    errorFrame.errorContainer.show()
//                    errorFrame.errorMessage.text =
//                        resources.getString(R.string.error_message_pattern)
//                            .getStringFormat(
//                                resources.getString(R.string.error),
//                                throwable.message
//                            )
//                    errorFrame.errorActionButton.setOnClickListener {
//                        getMovie()
//                    }
//                }
//            }
//
//        })
//    }
    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val localDetailsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            when (intent?.getStringExtra(RESULT_EXTRA)) {
                SUCCESS_RESULT -> {
                    intent.getParcelableExtra<MovieDTO>(MOVIE_DETAILS_EXTRA)?.let {
                        fillDetail(it)
                    }
                }
                else -> {

                }
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(localDetailsReceiver, IntentFilter(MOVIE_DETAILS_INTENT_FILTER))
    }

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

        getMovie()

        binding.favoritesButton.setOnClickListener {
            Toast.makeText(context, "To favorites!", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getMovie() {
        requireActivity().startService(
            Intent(requireContext(), MovieDetailsService::class.java).apply {
                putExtra(LOCALE_EXTRA, MainActivity.getDefaultLocaleString())
                putExtra(ID_EXTRA, movieId)
            }
        )
    }

    private fun fillDetail(movie: MovieDTO) {
        val releaseYear = movie.release_date?.subSequence(0, 4).toString()
        binding.apply {
            errorFrame.errorContainer.hide()
            loadingFrame.loadingContainer.hide()

            title.text = movie.title
            originalTitle.text = resources.getString(R.string.detail_orig_title_pattern)
                .getStringFormat(movie.original_title, releaseYear)
            image.setImageResource(R.drawable.dummy)
            genres.text = movie.genres?.let { getGenresNames(it) }
            duration.text = resources.getString(R.string.detail_duration_pattern)
                .getStringFormat(
                    movie.runtime,
                    resources.getString(R.string.minute),
                    movie.runtime?.getFormatDuration()
                )
            rating.text = resources.getString(R.string.detail_rating_pattern)
                .getStringFormat(movie.vote_average, movie.vote_count)
            budget.text = resources.getString(R.string.detail_budget_pattern)
                .getStringFormat(resources.getString(R.string.budget), movie.budget)
            revenue.text = resources.getString(R.string.detail_revenue_pattern)
                .getStringFormat(resources.getString(R.string.revenue), movie.revenue)
            released.text = resources.getString(R.string.detail_release_date_pattern)
                .getStringFormat(
                    resources.getString(R.string.released),
                    movie.release_date?.formatDate()
                )
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

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(localDetailsReceiver)
        super.onDestroy()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}