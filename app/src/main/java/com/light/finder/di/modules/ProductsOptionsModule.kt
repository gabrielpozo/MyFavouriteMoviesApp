package com.light.finder.di.modules

import com.light.presentation.viewmodels.ProductsOptionsViewModel
import com.light.usecases.*
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class ProductsOptionsModule {

    @Provides
    fun getWattageVariationsUseCase() =
        GetWattageVariationsUseCase()

    @Provides
    fun getColorVariationsUseCase() =
        GetColorVariationsUseCase()

    @Provides
    fun getFinishVariationsUseCase() =
        GetFinishVariationsUseCase()


    @Provides
    fun productsOptionsViewModel(
    ) = ProductsOptionsViewModel(
        Dispatchers.Main,
        getWattageVariationsUseCase(),
        getColorVariationsUseCase(),
        getFinishVariationsUseCase()
    )
}

@Subcomponent(modules = [(ProductsOptionsModule::class)])
interface ProductsOptionsComponent {
    val productsOptionsViewModel: ProductsOptionsViewModel
}