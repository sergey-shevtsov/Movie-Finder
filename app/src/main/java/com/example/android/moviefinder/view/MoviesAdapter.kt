package com.example.android.moviefinder.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.MovieItemBinding
import com.example.android.moviefinder.model.Movie

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MovieVH>() {

    class MovieVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = MovieItemBinding.bind(itemView)
        fun bind(movie: Movie) = with(binding) {
            posterImage.setImageResource(movie.imageId)
            title.text = movie.title
            releaseYear.text = movie.released.toString()
            ratingTv.text = movie.rating.toString()
        }
    }

    private lateinit var data: ArrayList<Movie>

    fun setData(data: ArrayList<Movie>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return MovieVH(view)
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

}