package com.gabriel.myfavouritemoviesapp.uimodels

import android.os.Parcelable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.gabriel.domain.model.Movie
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieUI(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val posterPath: String,
    val backdropPath: String,
    val originalLanguage: String,
    val originalTitle: String,
    val popularity: Double,
    val voteAverage: Double,
    var favorite: Boolean
) : Parcelable {
    companion object {
        private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/original/"

        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, urlPoster: String?) {
            urlPoster?.let {
                Glide.with(view.context).load("$POSTER_BASE_URL$urlPoster").into(view)
            }
        }
    }
}

fun Movie.toMovieUIModel(): MovieUI {
    return MovieUI(
        id,
        title,
        overview,
        releaseDate,
        posterPath,
        backdropPath,
        originalLanguage,
        originalTitle,
        popularity,
        voteAverage,
        favorite
    )
}