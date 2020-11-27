package com.gabriel.source

import com.gabriel.domain.Resource
import com.gabriel.domain.model.Movie

interface RemoteDataSource {
    suspend fun fetchMovies(): Resource<List<Movie>>
}