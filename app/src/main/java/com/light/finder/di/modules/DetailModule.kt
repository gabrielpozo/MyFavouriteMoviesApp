package com.light.finder.di.modules


import com.light.domain.CartItemCountRepository
import com.light.domain.CartRepository
import com.light.presentation.viewmodels.DetailViewModel
import com.light.usecases.GetAddToCartUseCase
import com.light.usecases.GetItemCountUseCase
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
    fun getItemCountUseCase(cartItemCountRepository: CartItemCountRepository) =
        GetItemCountUseCase(cartItemCountRepository)

    @Provides
    fun categoryViewModel(
        detailsUseCase: GetAddToCartUseCase,
        getItemCountUseCase: GetItemCountUseCase
    ) = DetailViewModel(
        detailsUseCase,
        getItemCountUseCase,
        Dispatchers.Main
    )

}

@Subcomponent(modules = [(DetailModule::class)])
interface DetailComponent {
    val detailViewModel: DetailViewModel
}
