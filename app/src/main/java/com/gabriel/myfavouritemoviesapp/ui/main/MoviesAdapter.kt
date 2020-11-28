package com.gabriel.myfavouritemoviesapp.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.myfavouritemoviesapp.R
import com.gabriel.myfavouritemoviesapp.extensions.basicDiffUtil
import com.gabriel.myfavouritemoviesapp.extensions.inflate
import com.gabriel.myfavouritemoviesapp.extensions.loadUrl
import com.gabriel.myfavouritemoviesapp.uimodels.MovieUI
import kotlinx.android.synthetic.main.item_movie.view.*

class MoviesAdapter(
    private val listener: (MovieUI) -> Unit
) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    var movies: List<MovieUI> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.id == new.id }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_movie, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener { listener(movie) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movie: MovieUI) {
            itemView.movieCover.loadUrl("https://image.tmdb.org/t/p/w185/${movie.posterPath}")
        }
    }
}