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
import com.example.android.moviefinder.model.MovieApiLoader
import com.example.android.moviefinder.model.MovieListDTO
import com.example.android.moviefinder.view.detail.DetailFragment
import com.example.android.moviefinder.view.hide
import com.example.android.moviefinder.view.hideHomeButton
import com.example.android.moviefinder.view.show
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

        val nowPlayingCategory = Category(
            resources.getString(R.string.now_playing),
            MoviesAdapter(),
            resources.getString(R.string.now_playing_request)
        )

        addCategory(nowPlayingCategory)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun addCategory(category: Category) {

        category.adapter.setOnItemClickListener(onItemClickListener)

        val view = LayoutInflater.from(context)
            .inflate(R.layout.category_section, binding.root, false)
        binding.container.addView(view)

        val categoryBinding = CategorySectionBinding.bind(view)
        categoryBinding.apply {
            titleTextView.text = category.title
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = category.adapter
        }

        val movieListLoader = MovieApiLoader.MovieListLoader(object :
            MovieApiLoader.MovieListLoader.MovieListLoaderListener {
            override fun onLoading() {
                categoryBinding.apply {
                    errorFrame.hide()
                    recyclerView.hide()
                    loadingTextView.show()
                }
            }

            override fun onLoaded(movieListDTO: MovieListDTO) {
                categoryBinding.apply {
                    loadingTextView.hide()
                    errorFrame.hide()
                    recyclerView.show()
                    movieListDTO.results?.let { category.adapter.setData(it) }
                }
            }

            override fun onFailed(throwable: Throwable) {
                categoryBinding.apply {
                    loadingTextView.hide()
                    recyclerView.hide()
                    errorFrame.show()
                    errorMessage.text =
                        "${resources.getString(R.string.error)}: ${throwable.message}"
                    errorActionButton.setOnClickListener {

                    }
                }
            }

        })

        movieListLoader.getMovieList("ru-RU", category.request)
    }
}