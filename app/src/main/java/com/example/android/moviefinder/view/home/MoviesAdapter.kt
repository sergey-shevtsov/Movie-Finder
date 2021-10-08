package com.example.android.moviefinder.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.MovieItemBinding
import com.example.android.moviefinder.model.MovieListDTO

const val POSTERS_URL_BASE = "https://image.tmdb.org/t/p/w200"

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MovieVH>() {

    private var data: Array<MovieListDTO.MovieItemDTO>? = null
    private var onItemClickListener: OnItemClickListener? = null

    fun setData(data: Array<MovieListDTO.MovieItemDTO>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return MovieVH(view)
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        data?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = data?.size ?: 0

    fun interface OnItemClickListener {
        fun onItemClicked(movie: MovieListDTO.MovieItemDTO)
    }

    inner class MovieVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val binding = MovieItemBinding.bind(itemView)
        private lateinit var movie: MovieListDTO.MovieItemDTO

        fun bind(movie: MovieListDTO.MovieItemDTO) {
            this.movie = movie

            itemView.setOnClickListener {
                onItemClickListener?.onItemClicked(movie)
            }

            binding.apply {
                posterImage.load("${POSTERS_URL_BASE}${movie.poster_path}")
                title.text = movie.title
                released.text = movie.release_date?.subSequence(0, 4).toString()
                ratingTv.text = movie.vote_average.toString()
            }
        }
    }

}