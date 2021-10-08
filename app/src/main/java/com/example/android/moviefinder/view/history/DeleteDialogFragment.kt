package com.example.android.moviefinder.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.HistoryFragmentDeleteDialogBinding

class DeleteDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(): DeleteDialogFragment = DeleteDialogFragment()
    }

    private var contractFragment: DialogCallbackContract? = null

    fun setContractFragment(contractFragment: DialogCallbackContract) {
        this.contractFragment = contractFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.history_fragment_delete_dialog, container, false)


        HistoryFragmentDeleteDialogBinding.bind(view).apply {

            positiveButton.setOnClickListener {
                dismiss()
                contractFragment?.sendDialogResult(true)
            }

            negativeButton.setOnClickListener { dismiss() }
        }
        return view
    }

    interface DialogCallbackContract {
        fun sendDialogResult(result: Boolean)
    }
}