package com.gabriel.domain

import com.gabriel.domain.models.Movie

interface MoviesRepository {
    suspend fun getPopularMovies(): Resource<List<Movie>>
}