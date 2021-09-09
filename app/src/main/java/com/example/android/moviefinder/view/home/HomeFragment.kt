package com.example.android.moviefinder.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.CategorySectionBinding
import com.example.android.moviefinder.databinding.HomeFragmentBinding
import com.example.android.moviefinder.model.Movie
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
    private val recommendedMoviesAdapter: MoviesAdapter by lazy {
        MoviesAdapter()
    }
    private val topRatedMoviesAdapter: MoviesAdapter by lazy {
        MoviesAdapter()
    }
    private val comedyMoviesAdapter: MoviesAdapter by lazy {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addCategory(
            getStringRes(R.string.recommended),
            recommendedMoviesAdapter,
            Category.RECOMMENDED
        )
        addCategory(
            getStringRes(R.string.top_rated),
            topRatedMoviesAdapter,
            Category.TOP_RATED
        )
        addCategory(
            getStringRes(R.string.comedy),
            comedyMoviesAdapter,
            Category.COMEDY
        )
    }

    private fun getStringRes(id: Int): String {
        return resources.getString(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addCategory(
        title: String,
        adapter: MoviesAdapter,
        category: Category
    ) {
        adapter.setOnItemClickListener { movie ->
            activity?.supportFragmentManager?.let {
                val bundle = Bundle()
                bundle.putInt(DetailFragment.MOVIE_ID_KEY, movie.id)
                it.beginTransaction()
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

        viewModel.getLiveData(category).observe(viewLifecycleOwner) {
            renderData(it, adapter, view, category)
        }
        viewModel.getMovies(category)
    }

    private fun renderData(state: AppState, adapter: MoviesAdapter, view: View, category: Category) {
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
                    adapter.setData((state.data as List<Movie>))
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