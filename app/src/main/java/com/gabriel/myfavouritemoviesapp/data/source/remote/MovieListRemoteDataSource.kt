package com.gabriel.myfavouritemoviesapp.data.source.remote

import com.gabriel.domain.Resource
import com.gabriel.domain.model.Movie
import com.gabriel.myfavouritemoviesapp.data.mappers.mapServerMovieToDomain
import com.gabriel.source.RemoteDataSource

class MovieListRemoteDataSource() : BaseDataSource<MovieDtoResult, List<Movie>>(), RemoteDataSource {

    override suspend fun fetchMovies(): Resource<List<Movie>> = getResult {
        TheMovieDb.service.getPopularMoviesAsync()
    }

    override fun mapResultToDomainModel(movieDtoResult: MovieDtoResult): List<Movie> = movieDtoResult.results.map(mapServerMovieToDomain)
}