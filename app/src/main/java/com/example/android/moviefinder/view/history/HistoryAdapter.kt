package com.example.android.moviefinder.view.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.HistoryItemBinding
import com.example.android.moviefinder.model.local.HistoryEntity
import com.example.android.moviefinder.view.formatToPattern

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var data: List<HistoryEntity>? = null
    private var onItemClickListener: OnItemClickListener? = null

    fun setData(data: List<HistoryEntity>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        data?.let { holder.bind(it[position]) }
    }

    override fun getItemCount(): Int = data?.size ?: 0

    fun interface OnItemClickListener {
        fun onItemClick(historyEntity: HistoryEntity)
    }

    inner class HistoryViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(historyEntity: HistoryEntity) {
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(historyEntity)
            }

            HistoryItemBinding.bind(itemView).apply {

                timestamp.text = historyEntity.timestamp.formatToPattern("dd.MM.yyyy HH:mm")
                title.text = historyEntity.title
                releasedYear.text = historyEntity.releasedYear
                voteAverage.text = historyEntity.voteAverage.toString()
                note.text = historyEntity.note

            }
        }

    }
}