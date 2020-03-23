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
    fun getAvailableSelectedFilterUseCase() =
        GetAvailableSelectedFilterUseCase()

    @Provides
    fun getNewSelectedProduct() =
        GetNewSelectedProduct()


    @Provides
    fun productsOptionsViewModel(
    ) = ProductsOptionsViewModel(
        Dispatchers.Main,
        getWattageVariationsUseCase(),
        getColorVariationsUseCase(),
        getFinishVariationsUseCase(),
        getAvailableSelectedFilterUseCase(),
        getNewSelectedProduct()
    )
}

@Subcomponent(modules = [(ProductsOptionsModule::class)])
interface ProductsOptionsComponent {
    val productsOptionsViewModel: ProductsOptionsViewModel
}