package com.example.android.moviefinder.view.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.CategorySectionBinding
import com.example.android.moviefinder.databinding.HomeFragmentBinding
import com.example.android.moviefinder.model.MovieListDTO
import com.example.android.moviefinder.view.detail.DetailFragment
import com.example.android.moviefinder.view.getStringFormat
import com.example.android.moviefinder.view.hide
import com.example.android.moviefinder.view.hideHomeButton
import com.example.android.moviefinder.view.show
import com.example.android.moviefinder.viewmodel.AppState
import com.example.android.moviefinder.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    private val onItemClickListener: MoviesAdapter.OnItemClickListener by lazy {
        MoviesAdapter.OnItemClickListener { movie ->
            activity?.supportFragmentManager?.let { manager ->
                val bundle = Bundle()
                movie.id?.let { id ->
                    bundle.putInt(DetailFragment.MOVIE_ID_KEY, id)
                }
                manager.beginTransaction()
                    .replace(R.id.fragment_container, DetailFragment.newInstance(bundle))
                    .addToBackStack("")
                    .commit()
            }
        }
    }

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)

        activity?.hideHomeButton()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addNowPlayingCategory()
        addPopularCategory()
        addTopRatedCategory()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun addNowPlayingCategory() {

        val adapter = MoviesAdapter()

        val view = LayoutInflater.from(context)
            .inflate(R.layout.category_section, binding.root, false)

        renderCategory(resources.getString(R.string.now_playing), view, adapter)

        viewModel.liveDataNowPlaying.observe(viewLifecycleOwner, getObserver(view, adapter))

        viewModel.getNowPlayingMovieListFromRemoteSource()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun addPopularCategory() {

        val adapter = MoviesAdapter()

        val view = LayoutInflater.from(context)
            .inflate(R.layout.category_section, binding.root, false)

        renderCategory(resources.getString(R.string.popular), view, adapter)

        viewModel.liveDataPopular.observe(viewLifecycleOwner, getObserver(view, adapter))

        viewModel.getPopularMovieListFromRemoteSource()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun addTopRatedCategory() {

        val adapter = MoviesAdapter()

        val view = LayoutInflater.from(context)
            .inflate(R.layout.category_section, binding.root, false)

        renderCategory(resources.getString(R.string.top_rated), view, adapter)

        viewModel.liveDataTopRated.observe(viewLifecycleOwner, getObserver(view, adapter))

        viewModel.getTopRatedMovieListFromRemoteSource()
    }

    private fun renderCategory(title: String, view: View, adapter: MoviesAdapter) {
        CategorySectionBinding.bind(view).apply {
            titleTextView.text = title
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = adapter
        }

        adapter.setOnItemClickListener(onItemClickListener)

        binding.container.addView(view)
    }

    private fun getObserver(view: View, adapter: MoviesAdapter): Observer<AppState> {
        return Observer<AppState> { state ->
            CategorySectionBinding.bind(view).apply {
                when (state) {
                    is AppState.Loading -> {
                        errorFrame.errorContainer.hide()
                        loadingFrame.loadingContainer.show()
                    }
                    is AppState.Success<*> -> {
                        errorFrame.errorContainer.hide()
                        loadingFrame.loadingContainer.hide()
                        (state.data as MovieListDTO).results?.let { adapter.setData(it) }
                    }
                    is AppState.Error -> {
                        loadingFrame.loadingContainer.hide()
                        errorFrame.errorContainer.show()
                        showError(view, state.t)
                    }
                }
            }
        }
    }

    private fun showError(view: View, t: Throwable) {
        CategorySectionBinding.bind(view).apply {
            errorFrame.errorMessage.text =
                resources.getString(R.string.error_message_pattern)
                    .getStringFormat(
                        resources.getString(R.string.error),
                        t.message
                    )
            errorFrame.errorActionButton.setOnClickListener {
                when (titleTextView.text) {
                    resources.getString(R.string.now_playing) ->
                        viewModel.getNowPlayingMovieListFromRemoteSource()
                    resources.getString(R.string.popular) ->
                        viewModel.getPopularMovieListFromRemoteSource()
                    resources.getString(R.string.top_rated) ->
                        viewModel.getTopRatedMovieListFromRemoteSource()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}