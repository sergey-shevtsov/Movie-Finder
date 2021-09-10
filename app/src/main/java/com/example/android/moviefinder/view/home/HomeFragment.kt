package com.example.android.moviefinder.view.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.CategorySectionBinding
import com.example.android.moviefinder.databinding.HomeFragmentBinding
import com.example.android.moviefinder.model.MovieListDTO
import com.example.android.moviefinder.view.detail.DetailFragment
import com.example.android.moviefinder.view.hide
import com.example.android.moviefinder.view.hideHomeButton
import com.example.android.moviefinder.view.show
import com.example.android.moviefinder.viewmodel.AppState
import com.example.android.moviefinder.viewmodel.Category
import com.example.android.moviefinder.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }
    private val nowPlayingMoviesAdapter: MoviesAdapter by lazy {
        MoviesAdapter()
    }
    private val popularMoviesAdapter: MoviesAdapter by lazy {
        MoviesAdapter()
    }
    private val topRatedMoviesAdapter: MoviesAdapter by lazy {
        MoviesAdapter()
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
        addCategory(
            getStringRes(R.string.now_playing),
            nowPlayingMoviesAdapter,
            Category.NOW_PLAYING
        )
//        addCategory(
//            getStringRes(R.string.popular),
//            popularMoviesAdapter,
//            Category.POPULAR
//        )
//        addCategory(
//            getStringRes(R.string.top_rated),
//            topRatedMoviesAdapter,
//            Category.TOP_RATED
//        )
    }

    private fun getStringRes(id: Int): String {
        return resources.getString(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun addCategory(
        title: String,
        adapter: MoviesAdapter,
        category: Category
    ) {
        adapter.setOnItemClickListener { movie ->
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

        val view = LayoutInflater.from(context)
            .inflate(R.layout.category_section, binding.root, false)
        binding.container.addView(view)

        CategorySectionBinding.bind(view).apply {
            titleTextView.text = title
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = adapter
        }

        viewModel.getLiveData(category).observe(viewLifecycleOwner) { state ->
            renderData(state, adapter, view, category)
        }
        viewModel.getMovies(category)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun renderData(
        state: AppState,
        adapter: MoviesAdapter,
        view: View,
        category: Category
    ) {
        CategorySectionBinding.bind(view).apply {

            recyclerView.hide()
            errorFrame.hide()
            loadingTextView.hide()

            when (state) {
                is AppState.Loading -> {
                    loadingTextView.show()
                }
                is AppState.Success -> {
                    recyclerView.show()
                    adapter.setData((state.data as Array<MovieListDTO.MovieItemDTO>))
                }
                is AppState.Error -> {
                    errorFrame.show()
                    errorMessage.text =
                        "${resources.getString(R.string.error)}: ${state.error.message}"
                    errorActionButton.setOnClickListener {
                        viewModel.getMovies(category)
                    }
                }
            }

        }
    }
}