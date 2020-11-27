package com.gabriel.myfavouritemoviesapp.di.modules

import com.gabriel.myfavouritemoviesapp.ui.main.MoviesViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class MoviesModule {
    @Provides
    fun movieViewModel(
    ) = MoviesViewModel(
        Dispatchers.Main
    )
}

@Subcomponent(modules = [(MoviesModule::class)])
interface MoviesComponent {
    val movieViewModel: MoviesViewModel
}