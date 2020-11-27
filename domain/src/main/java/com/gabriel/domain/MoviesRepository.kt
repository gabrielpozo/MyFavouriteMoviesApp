package com.gabriel.domain

import com.gabriel.domain.model.Movie

interface MoviesRepository {
    suspend fun getPopularMovies(): Resource<List<Movie>>
}