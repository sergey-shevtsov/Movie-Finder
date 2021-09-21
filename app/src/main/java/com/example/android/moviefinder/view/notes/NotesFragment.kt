package com.example.android.moviefinder.view.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.FavoritesFragmentBinding
import com.example.android.moviefinder.databinding.NotesFragmentBinding
import com.example.android.moviefinder.view.hideHomeButton
import com.example.android.moviefinder.view.showSnackBarMessage
import com.example.android.moviefinder.viewmodel.FavoritesViewModel
import com.example.android.moviefinder.viewmodel.NotesViewModel

class NotesFragment : Fragment() {

    companion object {
        fun newInstance() = NotesFragment()
    }

    private val viewModel: ViewModel by lazy {
        ViewModelProvider(this).get(NotesViewModel::class.java)
    }
    private var _binding: NotesFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NotesFragmentBinding.inflate(inflater, container, false)

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