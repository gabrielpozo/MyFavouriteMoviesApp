package com.gabriel.usecases

import com.gabriel.domain.MoviesRepository
import com.gabriel.domain.Resource
import com.gabriel.domain.models.Movie

class GetMoviesUseCase(private val moviesRepository: MoviesRepository) : MoviesBaseUseCase<List<Movie>>() {

    override suspend fun useCaseExecution(): Resource<List<Movie>> = moviesRepository.getPopularMovies()
}