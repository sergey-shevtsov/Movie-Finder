package com.example.android.moviefinder.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.moviefinder.databinding.HomeFragmentBinding
import com.example.android.moviefinder.viewmodel.AppState
import com.example.android.moviefinder.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private val firstSectionAdapter = MoviesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.firstSectionRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.firstSectionRecycler.adapter = firstSectionAdapter

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.liveData.observe(viewLifecycleOwner, { state ->
            renderData(state, firstSectionAdapter)
        })
        viewModel.getFirstSectionMovies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(state: AppState, adapter: MoviesAdapter) = with(binding) {
        when (state) {
            is AppState.Loading -> {
                firstSectionRecycler.visibility = View.GONE
                errorContainer.visibility = View.GONE
                firstSectionLoading.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                firstSectionLoading.visibility = View.GONE
                errorContainer.visibility = View.GONE
                firstSectionRecycler.visibility = View.VISIBLE
                adapter.setData(state.movies)
            }
            is AppState.Error -> {
                firstSectionRecycler.visibility = View.GONE
                firstSectionLoading.visibility = View.GONE
                errorMessage.text = "Ошибка: ${state.error.message}"
                errorButton.setOnClickListener {
                    viewModel.getFirstSectionMovies()
                }
                errorContainer.visibility = View.VISIBLE
            }
        }
    }
}