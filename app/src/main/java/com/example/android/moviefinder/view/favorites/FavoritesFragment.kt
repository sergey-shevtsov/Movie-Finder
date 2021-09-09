package com.example.android.moviefinder.view.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.FavoritesFragmentBinding
import com.example.android.moviefinder.view.hideHomeButton
import com.example.android.moviefinder.view.showSnackBarMessage
import com.example.android.moviefinder.viewmodel.FavoritesViewModel

class FavoritesFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private val viewModel: ViewModel by lazy {
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
        view.showSnackBarMessage(R.string.in_developing)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}