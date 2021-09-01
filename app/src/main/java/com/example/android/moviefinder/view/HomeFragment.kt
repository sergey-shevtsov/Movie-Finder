package com.example.android.moviefinder.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.CategorySectionBinding
import com.example.android.moviefinder.databinding.HomeFragmentBinding
import com.example.android.moviefinder.model.Movie
import com.example.android.moviefinder.viewmodel.AppState
import com.example.android.moviefinder.viewmodel.HomeViewModel
import java.util.*

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter = MoviesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        adapter.setOnItemClickListener(object : MoviesAdapter.OnItemClickListener {
            override fun onItemClicked(item: View, movie: Movie) {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.fragment_container, DetailFragment.newInstance())
                    ?.addToBackStack(null)
                    ?.commit()
            }

        })
        createCategory("Category 1", adapter, viewModel.getData())
        createCategory("Category 2", adapter, viewModel.getData())
        createCategory("Category 3", adapter, viewModel.getData())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createCategory(title: String, adapter: MoviesAdapter, liveData: LiveData<AppState>) {
        val view = LayoutInflater.from(context).inflate(R.layout.category_section, binding.root, false)
        binding.container.addView(view)
        val sectionBinding = CategorySectionBinding.bind(view)
        sectionBinding.title.text = title
        sectionBinding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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
                    errorLinear.visibility = View.GONE
                    loadingTextView.visibility = View.VISIBLE
                }
                is AppState.Success -> {
                    errorLinear.visibility = View.GONE
                    loadingTextView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    adapter.setData(state.movies)
                }
                is AppState.Error -> {
                    recyclerView.visibility = View.GONE
                    loadingTextView.visibility = View.GONE
                    errorLinear.visibility = View.VISIBLE
                    errorTextView.text = String.format(Locale.getDefault(), "%s: %s", resources.getString(R.string.error), state.error.message)
                    errorButton.setOnClickListener {
                        viewModel.getMovies()
                    }
                }
            }
        }
    }
}