package com.example.android.moviefinder.view.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.SettingsFragmentBinding
import com.example.android.moviefinder.view.hideHomeButton
import com.example.android.moviefinder.view.isChildMode
import com.example.android.moviefinder.view.showSnackBarMessage
import com.example.android.moviefinder.viewmodel.SettingsViewModel

const val CHILD_MODE_KEY = "ChildModeKey"

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private val viewModel: ViewModel by lazy {
        ViewModelProvider(this).get(SettingsViewModel::class.java)
    }
    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)

        activity?.hideHomeButton()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.childModeSwitch.isChecked = activity?.isChildMode() == true

        binding.childModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            sharedPref?.let {
                val editor = it.edit()
                editor.putBoolean(CHILD_MODE_KEY, isChecked)
                editor.apply()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}