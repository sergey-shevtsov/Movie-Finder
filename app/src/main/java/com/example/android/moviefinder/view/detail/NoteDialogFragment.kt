package com.example.android.moviefinder.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.DetailFragmentNoteDialogBinding
import com.example.android.moviefinder.view.toEditable

class NoteDialogFragment : DialogFragment() {

    companion object {
        private const val NOTE_EXTRA_KEY = "NoteExtraKey"

        fun newInstance(note: String): NoteDialogFragment {
            return NoteDialogFragment().also {
                val args = Bundle()
                args.putString(NOTE_EXTRA_KEY, note)
                it.arguments = args
            }
        }
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
            .inflate(R.layout.detail_fragment_note_dialog, container, false)

        arguments?.let { arguments ->
            val note = arguments.getString(NOTE_EXTRA_KEY)
            DetailFragmentNoteDialogBinding.bind(view).apply {
                note?.let { noteEditText.text = it.toEditable() }

                positiveButton.setOnClickListener {
                    dismiss()
                    contractFragment?.passDataBackToFragment(noteEditText.text.toString())
                }

                negativeButton.setOnClickListener { dismiss() }
            }
        }

        return view
    }

    interface DialogCallbackContract {
        fun passDataBackToFragment(note: String)
    }

}