package com.example.android.moviefinder.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.HistoryFragmentBinding
import com.example.android.moviefinder.model.local.HistoryEntity
import com.example.android.moviefinder.view.detail.DetailFragment
import com.example.android.moviefinder.view.hide
import com.example.android.moviefinder.view.hideHomeButton
import com.example.android.moviefinder.view.show
import com.example.android.moviefinder.viewmodel.AppState
import com.example.android.moviefinder.viewmodel.HistoryViewModel

class HistoryFragment : Fragment(), DeleteDialogFragment.DialogCallbackContract {

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }
    private var _binding: HistoryFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HistoryFragmentBinding.inflate(inflater, container, false)

        activity?.hideHomeButton()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HistoryAdapter()

        initRecyclerView(adapter)

        initViewModel(adapter)

        initDeleteAllButton()
    }

    private fun initRecyclerView(adapter: HistoryAdapter) {

        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickListener { historyEntity ->
            activity?.supportFragmentManager?.let { manager ->
                val bundle = Bundle()
                bundle.putInt(DetailFragment.MOVIE_ID_KEY, historyEntity.movieId)
                bundle.putParcelable(DetailFragment.HISTORY_EXTRA_KEY, historyEntity)

                manager.beginTransaction()
                    .replace(R.id.fragment_container, DetailFragment.newInstance(bundle))
                    .addToBackStack("")
                    .commit()
            }
        }
    }

    private fun initViewModel(adapter: HistoryAdapter) {
        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            binding.apply {

                when (state) {
                    is AppState.Loading -> {
                        emptyDataFrame.emptyDataContainer.hide()
                        loadingFrame.loadingContainer.show()
                    }
                    is AppState.Success<*> -> {
                        emptyDataFrame.emptyDataContainer.hide()
                        loadingFrame.loadingContainer.hide()
                        val data = state.data as List<HistoryEntity>
                        adapter.setData(data)
                        if (data.isEmpty()) emptyDataFrame.emptyDataContainer.show()
                    }
                    is AppState.Error -> {

                    }
                }

            }
        }

        viewModel.getAllHistory()
    }

    private fun initDeleteAllButton() {
        binding.deleteAllButton.setOnClickListener {
            val dialogFragment = DeleteDialogFragment.newInstance()
            dialogFragment.setContractFragment(this)
            dialogFragment.show(parentFragmentManager, "")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun sendDialogResult(result: Boolean) {
        if (result) viewModel.deleteAllHistory()
    }
}