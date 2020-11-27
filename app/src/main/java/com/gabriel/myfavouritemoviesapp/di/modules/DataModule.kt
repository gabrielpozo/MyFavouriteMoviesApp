package com.gabriel.myfavouritemoviesapp.di.modules

import com.gabriel.domain.MoviesRepository
import com.gabriel.myfavouritemoviesapp.data.source.remote.MovieListRemoteDataSource
import com.gabriel.repository.MoviesRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun getApiRepository(
        moviesListRemoteDataSource: MovieListRemoteDataSource,
    ): MoviesRepository =
        MoviesRepositoryImpl(
            moviesListRemoteDataSource
        )
}