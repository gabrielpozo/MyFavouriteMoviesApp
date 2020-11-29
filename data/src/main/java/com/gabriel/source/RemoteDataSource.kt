package com.gabriel.source

import com.gabriel.domain.Resource
import com.gabriel.domain.models.Movie

interface RemoteDataSource {
    suspend fun fetchMovies(): Resource<List<Movie>>
}