package com.example.android.moviefinder.view.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.FavoritesFragmentBinding
import com.example.android.moviefinder.model.local.FavoritesEntity
import com.example.android.moviefinder.view.detail.DetailFragment
import com.example.android.moviefinder.view.hide
import com.example.android.moviefinder.view.hideHomeButton
import com.example.android.moviefinder.view.show
import com.example.android.moviefinder.viewmodel.AppState
import com.example.android.moviefinder.viewmodel.FavoritesViewModel

class FavoritesFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private val viewModel: FavoritesViewModel by lazy {
        ViewModelProvider(this).get(FavoritesViewModel::class.java)
    }
    private var _binding: FavoritesFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoritesFragmentBinding.inflate(inflater, container, false)

        activity?.hideHomeButton()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FavoritesAdapter()

        initRecyclerView(adapter)

        initViewModel(adapter)
    }

    private fun initRecyclerView(adapter: FavoritesAdapter) {
        adapter.setOnItemClickListener { movieId ->
            activity?.supportFragmentManager?.let { manager ->
                val bundle = Bundle()
                bundle.putInt(DetailFragment.MOVIE_ID_KEY, movieId)

                manager.beginTransaction()
                    .replace(R.id.fragment_container, DetailFragment.newInstance(bundle))
                    .addToBackStack("")
                    .commit()
            }
        }
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = adapter
    }

    private fun initViewModel(adapter: FavoritesAdapter) {
        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            binding.apply {
                when (state) {
                    is AppState.Loading -> {
                        emptyDataFrame.emptyDataContainer.hide()
                        errorFrame.errorContainer.hide()
                        loadingFrame.loadingContainer.show()
                    }
                    is AppState.Success<*> -> {
                        emptyDataFrame.emptyDataContainer.hide()
                        errorFrame.errorContainer.hide()
                        loadingFrame.loadingContainer.hide()
                        val data = state.data as List<FavoritesEntity>
                        adapter.setData(data)
                        if (data.isEmpty()) emptyDataFrame.emptyDataContainer.show()
                    }
                    is AppState.Error -> {

                    }
                }
            }
        }

        viewModel.getAllFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}