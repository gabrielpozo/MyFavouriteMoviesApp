package com.gabriel.myfavouritemoviesapp.di.modules

import com.gabriel.myfavouritemoviesapp.data.source.remote.MovieListRemoteDataSource
import com.gabriel.source.RemoteDataSource
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    fun getMovieListRemoteDataSource(): RemoteDataSource =
        MovieListRemoteDataSource()
}