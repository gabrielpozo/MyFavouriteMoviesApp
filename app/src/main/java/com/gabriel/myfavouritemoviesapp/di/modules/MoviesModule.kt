package com.gabriel.myfavouritemoviesapp.di.modules

import com.gabriel.domain.MoviesRepository
import com.gabriel.myfavouritemoviesapp.ui.movielist.MoviesViewModel
import com.gabriel.usecases.GetMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class MoviesModule {

    @Provides
    fun getFavoriteMoviesUseCase(moviesRepository: MoviesRepository): GetMoviesUseCase {
        return GetMoviesUseCase(moviesRepository)
    }

    @Provides
    fun movieViewModel(
        getFavoriteMoviesUseCase: GetMoviesUseCase,
    ) = MoviesViewModel(
        getFavoriteMoviesUseCase,
        Dispatchers.Main
    )
}

@Subcomponent(modules = [(MoviesModule::class)])
interface MoviesComponent {
    val movieViewModel: MoviesViewModel
}