package com.example.android.moviefinder.view.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.ContactsItemBinding
import com.example.android.moviefinder.databinding.FavoritesItemBinding
import com.example.android.moviefinder.model.PhonebookContact
import com.example.android.moviefinder.model.local.FavoritesEntity
import com.example.android.moviefinder.view.favorites.FavoritesAdapter
import com.example.android.moviefinder.view.home.POSTERS_URL_BASE

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    private var onCallButtonClickListener: OnCallButtonClickListener? = null
    private var data: List<PhonebookContact>? = null

    fun setData(data: List<PhonebookContact>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onCallButtonClickListener: OnCallButtonClickListener) {
        this.onCallButtonClickListener = onCallButtonClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.contacts_item, parent, false)
        return ContactsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactsAdapter.ContactsViewHolder, position: Int) {
        data?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = data?.size ?: 0

    fun interface OnCallButtonClickListener {
        fun onCallButtonClick(phoneNumber: String?)
    }

    inner class ContactsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(phonebookContact: PhonebookContact) {
            val binding = ContactsItemBinding.bind(itemView)

            onCallButtonClickListener?.let { listener ->
                binding.callButton.setOnClickListener {
                    listener.onCallButtonClick(phonebookContact.phoneNumber)
                }
            }

            binding.apply {
                name.text = phonebookContact.name
                phoneNumber.text = phonebookContact.phoneNumber
            }
        }

    }

}