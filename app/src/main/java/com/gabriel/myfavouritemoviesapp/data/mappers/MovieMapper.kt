package com.gabriel.myfavouritemoviesapp.data.mappers

import com.gabriel.myfavouritemoviesapp.data.source.remote.MovieDto as ServerMovie

import com.gabriel.domain.model.Movie

val mapServerMovieToDomain: (ServerMovie) -> Movie = { movie ->
    Movie(
        movie.id,
        movie.title,
        movie.overview,
        movie.releaseDate ?: "no release date ",
        movie.posterPath ?: "no poster path",
        movie.backdropPath ?: "no poster backdropPath",
        movie.originalLanguage,
        movie.originalTitle,
        movie.popularity,
        movie.voteAverage,
        false
    )
}