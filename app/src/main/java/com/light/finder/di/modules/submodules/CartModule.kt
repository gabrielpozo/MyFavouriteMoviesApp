package com.light.finder.di.modules.submodules


import com.light.domain.CartItemCountRepository
import com.light.presentation.viewmodels.CartViewModel
import com.light.usecases.GetItemCountUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class CartModule {

    @Provides
    fun getItemCountUseCase(cartItemCountRepository: CartItemCountRepository) =
        GetItemCountUseCase(cartItemCountRepository)

    @Provides
    fun cartViewModel(
        getItemCountUseCase: GetItemCountUseCase
    ) = CartViewModel(
        getItemCountUseCase,
        Dispatchers.Main
    )

}

@Subcomponent(modules = [(CartModule::class)])
interface CartComponent {
    val cartViewModel: CartViewModel
}