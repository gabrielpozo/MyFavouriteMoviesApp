package com.light.finder.di.modules


import com.light.presentation.viewmodels.DetailViewModel
import com.light.usecases.GetDetailUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class DetailModule {

    @Provides
    fun getDetailsUseCase() =
        GetDetailUseCase()

    @Provides
    fun categoryViewModel(
        detailsUseCase: GetDetailUseCase
    ) = DetailViewModel(
        detailsUseCase,
        Dispatchers.Main
    )

}

@Subcomponent(modules = [(DetailModule::class)])
interface DetailComponent {
    val detailViewModel: DetailViewModel
}
