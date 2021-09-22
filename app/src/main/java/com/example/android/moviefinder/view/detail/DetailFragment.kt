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
import coil.api.load
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.DetailFragmentBinding
import com.example.android.moviefinder.model.GenresDTO
import com.example.android.moviefinder.model.MovieDetailsDTO
import com.example.android.moviefinder.model.local.HistoryEntity
import com.example.android.moviefinder.view.*
import com.example.android.moviefinder.viewmodel.AppState
import com.example.android.moviefinder.viewmodel.DetailsViewModel
import java.util.*

const val DETAILS_URL_BASE = "https://image.tmdb.org/t/p/w300"

class DetailFragment : Fragment(), NoteDialogFragment.DialogCallbackContract {

    companion object {
        const val MOVIE_ID_KEY = "MOVIE_EXTRA"
        const val HISTORY_EXTRA_KEY = "HISTORY_EXTRA"
        fun newInstance(bundle: Bundle): DetailFragment {
            return DetailFragment().also {
                it.arguments = bundle
            }
        }
    }

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }
    private val movieId: Int by lazy {
        arguments?.getInt(MOVIE_ID_KEY) ?: -1
    }

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    private var movieDetailsDTO: MovieDetailsDTO? = null
    private var historyEntity: HistoryEntity? = null

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

        initViewModel()

        historyEntity = arguments?.getParcelable(HISTORY_EXTRA_KEY)

        binding.noteButton.setOnClickListener {
            historyEntity?.note?.let {
                val noteDialogFragment = NoteDialogFragment.newInstance(it)
                noteDialogFragment.setContractFragment(this)
                noteDialogFragment.show(parentFragmentManager, "")
            }
        }

        binding.favoritesButton.setOnClickListener {
            Toast.makeText(context, "To favorites!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun passDataBackToFragment(note: String) {
        updateHistory(note)
    }

    private fun initViewModel() {
        viewModel.liveData.observe(viewLifecycleOwner,
            {
                binding.apply {
                    when (it) {
                        is AppState.Loading -> {
                            errorFrame.errorContainer.hide()
                            loadingFrame.loadingContainer.show()
                        }
                        is AppState.Success<*> -> {
                            errorFrame.errorContainer.hide()
                            loadingFrame.loadingContainer.hide()
                            movieDetailsDTO = it.data as MovieDetailsDTO
                            updateHistory(historyEntity?.note ?: "")
                            fillDetail()
                        }
                        is AppState.Error -> {
                            errorFrame.errorContainer.show()
                            loadingFrame.loadingContainer.hide()
                            showError(it.t)
                        }
                    }
                }
            })

        viewModel.getMovieDetailsFromRemoteSource(movieId)
    }

    private fun updateHistory(note: String) {
        movieDetailsDTO?.let {
            if (historyEntity == null) {
                historyEntity = HistoryEntity(
                    movieId = it.id!!,
                    title = it.title!!,
                    releasedYear = it.release_date!!.subSequence(0, 4).toString(),
                    voteAverage = it.vote_average!!,
                    timestamp = Calendar.getInstance().timeInMillis,
                    note = note
                )
                viewModel.insertHistory(historyEntity!!)
            } else {
                historyEntity!!.note = note
                viewModel.updateHistory(historyEntity!!)
            }
        }
    }

    private fun fillDetail() {
        movieDetailsDTO?.let {
            val releaseYear = it.release_date?.subSequence(0, 4).toString()
            binding.apply {
                title.text = it.title
                originalTitle.text = resources.getString(R.string.detail_orig_title_pattern)
                    .getStringFormat(it.original_title, releaseYear)
                image.load("${DETAILS_URL_BASE}${it.poster_path}")
                genres.text = it.genres?.let { getGenresNames(it) }
                duration.text = resources.getString(R.string.detail_duration_pattern)
                    .getStringFormat(
                        it.runtime,
                        resources.getString(R.string.minute),
                        it.runtime?.getFormatDuration()
                    )
                rating.text = resources.getString(R.string.detail_rating_pattern)
                    .getStringFormat(it.vote_average, it.vote_count)
                budget.text = resources.getString(R.string.detail_budget_pattern)
                    .getStringFormat(resources.getString(R.string.budget), it.budget)
                revenue.text = resources.getString(R.string.detail_revenue_pattern)
                    .getStringFormat(resources.getString(R.string.revenue), it.revenue)
                released.text = resources.getString(R.string.detail_release_date_pattern)
                    .getStringFormat(
                        resources.getString(R.string.released),
                        it.release_date?.formatDate()
                    )
                overview.text = it.overview
            }
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

    private fun showError(t: Throwable) {
        binding.apply {
            errorFrame.errorMessage.text = resources.getString(R.string.error_message_pattern)
                .getStringFormat(
                    resources.getString(R.string.error),
                    t.message
                )
            errorFrame.errorActionButton.setOnClickListener {
                viewModel.getMovieDetailsFromRemoteSource(movieId)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}