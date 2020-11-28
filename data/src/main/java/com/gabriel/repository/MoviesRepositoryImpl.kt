package com.gabriel.repository

import com.gabriel.domain.MoviesRepository
import com.gabriel.domain.Resource
import com.gabriel.domain.models.Movie
import com.gabriel.source.RemoteDataSource

class MoviesRepositoryImpl(private val remoteDataSource: RemoteDataSource) : MoviesRepository {

    override suspend fun getPopularMovies(): Resource<List<Movie>> = remoteDataSource.fetchMovies()
}