package com.light.finder.di.modules


import com.light.domain.CartRepository
import com.light.presentation.viewmodels.DetailViewModel
import com.light.usecases.GetAddToCartUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class DetailModule {

    @Provides
    fun getDetailsUseCase(cartRepository: CartRepository) =
        GetAddToCartUseCase(cartRepository)

    @Provides
    fun categoryViewModel(
        detailsUseCase: GetAddToCartUseCase
    ) = DetailViewModel(
        detailsUseCase,
        Dispatchers.Main
    )

}

@Subcomponent(modules = [(DetailModule::class)])
interface DetailComponent {
    val detailViewModel: DetailViewModel
}
