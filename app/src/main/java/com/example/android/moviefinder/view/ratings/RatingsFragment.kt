package com.example.android.moviefinder.view.ratings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.moviefinder.databinding.RatingsFragmentBinding
import com.example.android.moviefinder.view.hideHomeButton
import com.example.android.moviefinder.viewmodel.RatingsViewModel

class RatingsFragment : Fragment() {

    companion object {
        fun newInstance() = RatingsFragment()
    }

    private val viewModel: ViewModel by lazy {
        ViewModelProvider(this).get(RatingsViewModel::class.java)
    }
    private var _binding: RatingsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RatingsFragmentBinding.inflate(inflater, container, false)

        activity?.hideHomeButton()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}