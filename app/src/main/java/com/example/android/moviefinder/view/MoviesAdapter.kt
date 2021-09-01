package com.example.android.moviefinder.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.MovieItemBinding
import com.example.android.moviefinder.model.Movie

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MovieVH>() {

    class MovieVH(itemView: View, onItemClickListener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        private val binding = MovieItemBinding.bind(itemView)
        private lateinit var movie: Movie

        init {
            itemView.setOnClickListener {
                onItemClickListener?.onItemClicked(itemView, movie)
            }
        }

        fun bind(movie: Movie) {
            this.movie = movie

            binding.apply {
                posterImage.setImageResource(movie.imageId)
                title.text = movie.title
                releaseYear.text = movie.released.toString()
                ratingTv.text = movie.rating.toString()
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(item: View, movie: Movie)
    }

    private lateinit var data: ArrayList<Movie>
    private var onItemClickListener: OnItemClickListener? = null

    fun setData(data: ArrayList<Movie>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return MovieVH(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

}