package com.example.android.moviefinder.view.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.CategorySectionBinding
import com.example.android.moviefinder.databinding.HomeFragmentBinding
import com.example.android.moviefinder.model.Movie
import com.example.android.moviefinder.view.detail.DetailFragment
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

        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
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
        createCategory("Category title", adapter, viewModel.getData())
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
                    loadingTextView.visibility = View.VISIBLE
                }
                is AppState.Success -> {
                    loadingTextView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    adapter.setData((state.data as List<Movie>))
                }
                is AppState.Error -> {
                }
            }
        }
    }
}