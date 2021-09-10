package com.example.android.moviefinder.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.moviefinder.R
import com.example.android.moviefinder.databinding.MovieItemBinding
import com.example.android.moviefinder.model.MovieListDTO

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MovieVH>() {

    class MovieVH(itemView: View, onItemClickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(itemView) {
        private val binding = MovieItemBinding.bind(itemView)
        private lateinit var movie: MovieListDTO.MovieItemDTO

        init {
            itemView.setOnClickListener {
                onItemClickListener?.onItemClicked(movie)
            }
        }

        fun bind(movie: MovieListDTO.MovieItemDTO) {
            this.movie = movie
            val releaseYear = movie.release_date?.subSequence(0, 4).toString()

            binding.apply {
                posterImage.setImageResource(R.drawable.dummy)
                title.text = movie.title
                released.text = releaseYear
                ratingTv.text = movie.vote_average.toString()
            }
        }
    }

    fun interface OnItemClickListener {
        fun onItemClicked(movie: MovieListDTO.MovieItemDTO)
    }

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
        return MovieVH(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        data?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = data?.size ?: 0

}