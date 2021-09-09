package com.example.android.moviefinder.view.home

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
        private var movie: Movie? = null

        init {
            itemView.setOnClickListener {
                onItemClickListener?.onItemClicked(movie)
            }
        }

        fun bind(movie: Movie) {
            this.movie = movie
            val releaseYear = movie.released.subSequence(6, 10).toString()

            binding.apply {
                posterImage.setImageResource(movie.imageId)
                title.text = movie.title
                released.text = releaseYear
                ratingTv.text = movie.rating.toString()
            }
        }
    }

    fun interface OnItemClickListener {
        fun onItemClicked(movie: Movie?)
    }

    private lateinit var data: List<Movie>
    private var onItemClickListener: OnItemClickListener? = null

    fun setData(data: List<Movie>) {
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