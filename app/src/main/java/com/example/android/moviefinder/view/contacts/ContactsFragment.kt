package com.example.android.moviefinder.view.contacts

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.moviefinder.databinding.ContactsFragmentBinding
import com.example.android.moviefinder.model.PhonebookContact
import com.example.android.moviefinder.view.showHomeButton
import com.example.android.moviefinder.viewmodel.AppState
import com.example.android.moviefinder.viewmodel.ContactsViewModel

class ContactsFragment : Fragment() {
    companion object {
        fun newInstance(): ContactsFragment = ContactsFragment()
    }

    private val viewModel: ContactsViewModel by lazy {
        ViewModelProvider(this).get(ContactsViewModel::class.java)
    }

    private var _binding: ContactsFragmentBinding? = null
    private val binding get() = _binding!!

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            when {
                result -> viewModel.getContacts(requireActivity())
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_CONTACTS
                ) -> {
                    Toast.makeText(
                        context,
                        "Go to app settings and enable permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> Toast.makeText(context, "T_T", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactsFragmentBinding.inflate(inflater, container, false)

        activity?.showHomeButton()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ContactsAdapter()
        initRecyclerView(adapter)
        initViewModel(adapter)

        permissionResult.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun initRecyclerView(adapter: ContactsAdapter) {
        adapter.setOnItemClickListener {
            it?.let {
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", it, null))
                startActivity(intent)
            }
        }

        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter
    }

    private fun initViewModel(adapter: ContactsAdapter) {
        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AppState.Loading -> {

                }
                is AppState.Success<*> -> {
                    adapter.setData(state.data as List<PhonebookContact>)
                }
                is AppState.Error -> {

                }
            }
        }
    }
}