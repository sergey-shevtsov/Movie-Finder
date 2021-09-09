package com.example.android.moviefinder.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.CategorySectionBinding
import com.example.android.moviefinder.databinding.HomeFragmentBinding
import com.example.android.moviefinder.model.Movie
import com.example.android.moviefinder.view.detail.DetailFragment
import com.example.android.moviefinder.viewmodel.AppState
import com.example.android.moviefinder.viewmodel.HomeViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private val recommendedMoviesAdapter = MoviesAdapter()
    private val topRatedMoviesAdapter = MoviesAdapter()
    private val comedyMoviesAdapter = MoviesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)

        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        addCategory(resources.getString(R.string.recommended), recommendedMoviesAdapter, viewModel.getRecommendedMoviesLiveData())
        addCategory(resources.getString(R.string.top_rated), topRatedMoviesAdapter, viewModel.getTopRatedMoviesLiveData())
        addCategory(resources.getString(R.string.comedy), comedyMoviesAdapter, viewModel.getComedyMoviesLiveData())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addCategory(
        title: String,
        adapter: MoviesAdapter,
        liveData: LiveData<AppState>
    ) {
        adapter.setOnItemClickListener { item, movie ->
            val manager = activity?.supportFragmentManager
            if (manager != null) {
                val bundle = Bundle()
                bundle.putInt(DetailFragment.MOVIE_KEY, movie.id)
                manager.beginTransaction()
                    .replace(R.id.fragment_container, DetailFragment.newInstance(bundle))
                    .addToBackStack("")
                    .commit()
            }
        }

        val view =
            LayoutInflater.from(context).inflate(R.layout.category_section, binding.root, false)
        binding.container.addView(view)
        val sectionBinding = CategorySectionBinding.bind(view)
        sectionBinding.title.text = title
        sectionBinding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        sectionBinding.recyclerView.adapter = adapter
        liveData.observe(viewLifecycleOwner) {
            renderData(it, adapter, view)
        }
    }

    private fun renderData(state: AppState, adapter: MoviesAdapter, view: View) {
        val sectionBinding = CategorySectionBinding.bind(view)
        sectionBinding.apply {
            when (state) {
                is AppState.Loading -> {
                    recyclerView.visibility = View.GONE
                    loadingTextView.visibility = View.VISIBLE
                }
                is AppState.Success -> {
                    loadingTextView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    adapter.setData((state.data as List<Movie>))
                }
                is AppState.Error -> {
//                    loadingTextView.visibility = View.GONE
//                    recyclerView.visibility = View.GONE
//                    Snackbar.make(
//                        binding.root,
//                        "${resources.getString(R.string.error)}: ${state.error.message}",
//                        LENGTH_INDEFINITE
//                    )
//                        .setAction(resources.getString(R.string.reload)) {
//                            viewModel.getMovies()
//                        }.show()
                }
            }
        }
    }
}