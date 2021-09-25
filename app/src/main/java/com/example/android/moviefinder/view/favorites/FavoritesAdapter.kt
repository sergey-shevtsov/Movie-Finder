package com.example.android.moviefinder.view.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.FavoritesItemBinding
import com.example.android.moviefinder.model.local.FavoritesEntity
import com.example.android.moviefinder.view.home.POSTERS_URL_BASE

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    private var data: List<FavoritesEntity>? = null

    fun setData(data: List<FavoritesEntity>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.favorites_item, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        data?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = data?.size ?: 0

    fun interface OnItemClickListener {
        fun onItemClick(movieId: Int)
    }

    inner class FavoritesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(favoritesEntity: FavoritesEntity) {
            onItemClickListener?.let { listener ->
                itemView.setOnClickListener {
                    listener.onItemClick(favoritesEntity.movieId)
                }
            }

            FavoritesItemBinding.bind(itemView).apply {
                posterImage.load("${POSTERS_URL_BASE}${favoritesEntity.posterPath}")
                title.text = favoritesEntity.title
                releasedYear.text = favoritesEntity.releasedYear
                voteAverage.text = favoritesEntity.voteAverage.toString()
            }
        }

    }

}