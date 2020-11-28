package com.gabriel.myfavouritemoviesapp.di.modules

import com.gabriel.myfavouritemoviesapp.ui.detail.DetailViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class DetailMovieModule {

    @Provides
    fun detailMovieViewModel(
    ) = DetailViewModel(
        Dispatchers.Main
    )
}

@Subcomponent(modules = [(DetailMovieModule::class)])
interface DetailMovieComponent {
    val detailViewModel: DetailViewModel
}